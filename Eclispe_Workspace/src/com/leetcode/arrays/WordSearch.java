
package com.leetcode.arrays;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class WordSearch {

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

    /**
     * @param args
     */
    public static void main(String[] args) {
        char[][] board = {
                {
                        'A', 'B', 'C', 'E'
                }, {
                        'S', 'F', 'F', 'S'
                }, {
                        'A', 'D', 'E', 'E'
                }
        };
        System.out.println(exist(board, "ABCCED"));
        System.out.println(exist(board, "ABCESEEEFS"));
        System.out.println(exist(board, "SEE"));
        System.out.println(exist(board, "ABCB"));
    }

    static public boolean exist(char[][] board, String word) {
        int row = board.length;
        int col = 0;
        if (row > 0)
            col = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (search(board, row, col, i, j, word.toCharArray(), 0))
                        return true;
                }
            }
        }
        return false;
    }

    private static boolean search(char[][] board, int row, int col, int i, int j, char[] charArray,
            int index) {
        if (i < 0 || j < 0 || i > row || j > col)
            return false;
        if (board[i][j] != charArray[index]) {
            return false;
        }
        if (board[i][j] == charArray[index] && index == charArray.length - 1)
            return true;
        // This is the tricky part.
        // This ensures that if I reach here next time then there will be no match and it will
        // return false.
        // We proceed further and set it back to it's original state in last.
        board[i][j] = '*';
        boolean res = false;
        // Check in every possible direction if char is matched.
        // If in some direction charArray is found then simply returns true and
        // Do not check for other indexes.
        // That is why res is used. To avoid extra comparisions.
        if (i > 0) {
            res = search(board, row, col, i - 1, j, charArray, index + 1);
        }
        if (i < row - 1 && !res) {
            res = search(board, row, col, i + 1, j, charArray, index + 1);
        }
        if (j > 0 && !res) {
            res = search(board, row, col, i, j - 1, charArray, index + 1);
        }
        if (j < col - 1 && !res) {
            res = search(board, row, col, i, j + 1, charArray, index + 1);
        }
        board[i][j] = charArray[index];
        return res;
    }

}
