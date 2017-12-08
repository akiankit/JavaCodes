package com.codeforces;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

class CuttingBanner {

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
        String[][] possibilities = {
                {
                        "C", "ODEFORCES"
                }, {
                        "CO", "DEFORCES"
                }, {
                        "COD", "EFORCES"
                }, {
                        "CODE", "FORCES"
                }, {
                        "CODEF", "ORCES"
                }, {
                        "CODEFO", "RCES"
                }, {
                        "CODEFOR", "CES"
                }, {
                        "CODEFORC", "ES"
                }, {
                        "CODEFORCE", "S"
                },
        };
        String charsequence = "CODEFORCES";
        String read = ir.next();
        boolean possible = false;
        if(read.length() < 10){
            possible = false;
        } else if (read.contains(charsequence)) {
            int index = read.indexOf(charsequence);
            if (index == 0 || index == read.length() - charsequence.length())
                possible = true;
        }
        if (possible == false && read.length() >=10) {
            for (int i = 0; i < 9; i++) {
                boolean firstMatch = possibilities[i][0].equalsIgnoreCase(read.substring(0,
                        possibilities[i][0].length()));
                boolean secondMatch = possibilities[i][1].equalsIgnoreCase(read.substring(
                        read.length() - possibilities[i][1].length(), read.length()));
                if (firstMatch && secondMatch) {
                    possible = true;
                    break;
                }
            }
        }
        if (possible)
            ow.printLine("YES");
        else
            ow.printLine("NO");
        ow.close();
    }

}
