package com.misc.problems;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class SudokuSwap {

    static class InputReader {

        private InputStream stream;

        private byte[] buf = new byte[1024];

        private int curChar;

        private int numChars;

        private SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1)
                throw new InputMismatchException();
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public int readInt() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String next() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public long readLong() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public boolean isSpaceChar(int c) {
            if (filter != null)
                return filter.isSpaceChar(c);
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);
        }
    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public void printLine(String string) {
            writer.println(string);
        }

        public void close() {
            writer.close();
        }

        public void printLine(long i) {
            writer.println(i);
        }

    }

    public static void main(String[] args) {
        InputReader ir = new InputReader(System.in);
        OutputWriter ow = new OutputWriter(System.out);
        int tests = ir.readInt();
        for (int i = 0; i < tests; i++) {
            int[][] sudoku = new int[10][10];
            for (int j = 1; j < 10; j++) {
                for (int k = 1; k < 10; k++) {
                    sudoku[j][k] = ir.readInt();
                }
            }
            if (isSudokuValid(sudoku)) {
                ow.printLine("Case #" + (i + 1) + ":");
                ow.printLine("Serendipity");
            } else {
                int[] nums = new int[10];
                boolean rowCorrect = true;
                boolean colCorrect = true;
                for (int j = 1; j < 10; j++) {
                    if (!isRowCorrect(sudoku, j)) {
                        rowCorrect = false;
                        break;
                    }
                }
                if (!rowCorrect)
                    printSolutionInRowFaulty(ow, i, sudoku, nums);
                else {
                    for (int j = 1; j < 10; j++) {
                        if (!isColumnCorrect(sudoku, j)) {
                            colCorrect = false;
                            break;
                        }
                    }
                    if (!colCorrect)
                        printOutputInColFaulty(ow, i, sudoku, nums);
                }
            }
        }
        ow.close();
    }

    private static void printOutputInColFaulty(OutputWriter ow, int i, int[][] sudoku, int[] nums) {
        int col1 = 0, col2 = 0, row11 = 0, row12 = 0, row21 = 0, row22 = 0;
        for (int j = 1; j < 10; j++) {
            nums = new int[10];
            for (int k = 1; k < 10; k++) {
                if (nums[sudoku[k][j]] != 0) {
                    if (col1 == 0) {
                        col1 = j;
                        row11 = nums[sudoku[k][j]];
                        row12 = k;
                    } else {
                        col2 = j;
                        row21 = nums[sudoku[k][j]];
                        row22 = k;
                    }
                } else {
                    nums[sudoku[k][j]] = k;
                }
            }
        }
        int row1 = 0;
        int row2 = 0;
        swap(sudoku, row11, col1, row21, col2);
        if (isSudokuValid(sudoku)) {
            row1 = row11;
            row2 = row21;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
        swap(sudoku, row11, col1, row21, col2);
        swap(sudoku, row11, col1, row22, col2);
        if (isSudokuValid(sudoku)) {
            row1 = row11;
            row2 = row22;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
        swap(sudoku, row11, col1, row22, col2);
        swap(sudoku, row12, col1, row21, col2);
        if (isSudokuValid(sudoku)) {
            row1 = row12;
            row2 = row21;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
        swap(sudoku, row12, col1, row21, col2);
        swap(sudoku, row12, col1, row22, col2);
        if (isSudokuValid(sudoku)) {
            row1 = row12;
            row2 = row22;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
    }

    private static void printSolutionInRowFaulty(OutputWriter ow, int i, int[][] sudoku, int[] nums) {
        int row1 = 0, row2 = 0, col11 = 0, col12 = 0, col21 = 0, col22 = 0;
        for (int j = 1; j < 10; j++) {
            nums = new int[10];
            for (int k = 1; k < 10; k++) {
                if (nums[sudoku[j][k]] != 0) {
                    if (row1 == 0) {
                        row1 = j;
                        col11 = nums[sudoku[j][k]];
                        col12 = k;
                    } else {
                        row2 = j;
                        col21 = nums[sudoku[j][k]];
                        col22 = k;
                    }
                } else {
                    nums[sudoku[j][k]] = k;
                }
            }
        }
        int col1 = 0;
        int col2 = 0;
        swap(sudoku, row1, col11, row2, col21);
        if (isSudokuValid(sudoku)) {
            col1 = col11;
            col2 = col21;
            // System.out.println("case 1");
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
        swap(sudoku, row1, col11, row2, col21);
        swap(sudoku, row1, col11, row2, col22);
        if (isSudokuValid(sudoku)) {
            col1 = col11;
            col2 = col22;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
        swap(sudoku, row1, col11, row2, col22);
        swap(sudoku, row1, col12, row2, col21);
        if (isSudokuValid(sudoku)) {
            col1 = col12;
            col2 = col21;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
        }
        swap(sudoku, row1, col12, row2, col21);
        swap(sudoku, row1, col12, row2, col22);
        if (isSudokuValid(sudoku)) {
            col1 = col12;
            col2 = col22;
            getFormattedOutput(ow, row1, col1, row2, col2,i);
            /*
             * if (row1 > row2) { int temp = row2; row2 = row1; row1 = temp; temp = col2; col2 =
             * col1; col1 = temp; } else if (row1 == row2 && col1 > col2) { int temp = col2; col2 =
             * col1; col1 = temp; } // System.out.println("case 4"); ow.printLine("(" + row1 + "," +
             * col1 + ") <-> (" + row2 + "," + col2 + ")");
             */
        }
    }

    private static void getFormattedOutput(OutputWriter ow, int row1, int col1, int row2, int col2,
            int caseNo) {
        if (row1 > row2) {
            int temp = row2;
            row2 = row1;
            row1 = temp;
            temp = col2;
            col2 = col1;
            col1 = temp;
        } else if (row1 == row2 && col1 > col2) {
            int temp = col2;
            col2 = col1;
            col1 = temp;
        }
        ow.printLine("Case #" + (caseNo + 1) + ":");
        ow.printLine("(" + row1 + "," + col1 + ") <-> (" + row2 + "," + col2 + ")");
    }
    
    private static void swap(int[][] sudoku, int row1, int col1, int row2, int col2) {
        int temp = sudoku[row1][col1];
        sudoku[row1][col1] = sudoku[row2][col2];
        sudoku[row2][col2] = temp;
    }

    private static boolean isSudokuValid(int[][] sudoku) {
        for(int i=1;i<10;i++)
            if(!isColumnCorrect(sudoku, i))
                return false;
        for(int i=1;i<10;i++)
            if(!isRowCorrect(sudoku, i))
                return false;
        for(int i=1;i<10;i++)
            if(!isGridCorrect(sudoku, i))
                return false;
        return true;
    }
    
    private static boolean isGridCorrect(int[][] sudoku, int num) {
        num = num-1;
        int rowStart = (num/3)*3 +1;
        int colStart = (num%3)*3+1;
        int maxRow = rowStart+3;
        int maxCol = colStart+3;
        int nums[] = new int[10];
        for(int i=rowStart;i<maxRow;i++) {
            for(int j=colStart;j<maxCol;j++) {
                if(nums[sudoku[i][j]] != 0)
                    return false;
            }
        }
        return true;
    }
    private static boolean isColumnCorrect(int[][] sudoku, int col) {
        int[] nums = new int[10];
        for(int i=1;i<10;i++) {
            if(nums[sudoku[i][col]] != 0)
                return false;
            nums[sudoku[i][col]] = i;
        }
        return true;
    }
    
    private static boolean isRowCorrect(int[][] sudoku, int row) {
        int[] nums = new int[10];
        for(int i=1;i<10;i++) {
            if(nums[sudoku[row][i]] != 0)
                return false;
            nums[sudoku[row][i]] = i;
        }
        return true;
    }

}
