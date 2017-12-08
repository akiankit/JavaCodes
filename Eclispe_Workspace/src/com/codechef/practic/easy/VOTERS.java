package com.codechef.practic.easy;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Set;

class VOTERS{
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
        int n1 = ir.readInt();
        int n2 = ir.readInt();
        int n3 = ir.readInt();

        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for (int i = 0; i < n1 + n2 + n3; i++) {
            int n = ir.readInt();
            if (!counts.containsKey(n))
                counts.put(n, 1);
            else
                counts.put(n, 2);
        }
        int size = 0;
        Set<Integer> keySet = counts.keySet();
        for (Integer key : keySet) {
            if (counts.get(key) == 2) {
                size++;
            }
        }
        int k = 0;
        int[] res = new int[size];
        for (Integer key : keySet) {
            if (counts.get(key) >= 2) {
                res[k] = key;
                k++;
            }
        }

        Arrays.sort(res);
        ow.printLine(size);
        for (int i = 0; i < size; i++)
            ow.printLine(res[i]);
        ow.close();}
}