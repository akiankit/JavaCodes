package com.codechef.practic.easy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class CHCUBE {

    
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
        InputReader ir = new InputReader(System.in);
        OutputWriter ow = new OutputWriter(System.out);
        int tests = ir.readInt();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("black", 0);
        map.put("blue", 1);
        map.put("orange", 2);
        map.put("yellow", 3);
        map.put("green", 4);
        map.put("red", 5);
        for (int i = 0; i < tests; i++) {
            Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();
            map2.put(0, 0);
            map2.put(1, 0);
            map2.put(2, 0);
            map2.put(3, 0);
            map2.put(4, 0);
            map2.put(5, 0);
            int max = 0;
            int maxColorIndex = -1;
            int[] sides = new int[6];
            for (int j = 0; j < 6; j++) {
                String color = ir.next();
                // System.out.println(color);
                int colorIndex = map.get(color);
                if (max < map2.get(colorIndex) + 1) {
                    maxColorIndex = colorIndex;
                    max = map2.get(colorIndex) + 1;
                }
                sides[j] = colorIndex;
                map2.put(colorIndex, map2.get(colorIndex) + 1);
            }
            if (max > 4) {
                ow.printLine("YES");
            } else if (max < 3) {
                ow.printLine("NO");
            } else {
                int a = -1, b = -1, c = -1;
                for (int j = 0; j < 6; j++) {
                    if (sides[j] == maxColorIndex) {
                        if (j == 0 || j == 1) {
                            a = j >> 1;
                        } else if (j == 2 || j == 3) {
                            b = j >> 1;
                        } else if (j == 4 || j == 5) {
                            c = j >> 1;
                        }
                    }
                }
                if (a != 0 || b != 1 || c != 2) {
                    ow.printLine("NO");
                } else {
                    ow.printLine("YES");
                }
            }
        }
        ow.close();
    }

}
