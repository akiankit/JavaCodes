package com.codechef.practic.easy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

class SETDIFF {

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
     * let's use the previously calculated value ((i-1)th item).
     * the solution for ith element is related to the solution for first
     * i-1 elements.
     * S(n) = (S(n-1) + count(n-1) * diff(n, n-1)) * 2 + diff(n, n-1)
     * here S(n) is solution for first n elements.
     * count(n) is number of subsets can be formed using first n elements.
     * diff(n, n-1) is the difference b/w nth and (n-1)th element,
     * i. e. ar[i] - ar[i-1].
     * Theory: S(n-1) is the solution for n-1 elements, if we use the same
     * subsets the solution would be S(n-1) + count(n-1) * diff
     * as nth element is larger than n-1 th element by diff and so in every
     * calculation the difference will be diff.
     * now S(n-1) is when we use n-1 th element in subset (for S(n)
     * and S(n-1) when we don't.
     * So (S(n-1) + count * diff) comes in S(n) two times.
     * now we have additional subset of exactly two elements n-1th and nth.
     * so in this case the max - min will again be diff.
     *
     */
    public static void main(String[] args) {
        InputReader ir = new InputReader(System.in);
        OutputWriter ow = new OutputWriter(System.out);
        int tests = ir.readInt();
        int mod = 1000000007;
        for (int i = 0; i < tests; i++) {
            int N = ir.readInt();
            int A[] = new int[N];
            for (int j = 0; j < N; j++) {
                A[j] = ir.readInt();
            }
            Arrays.sort(A);
            if (N == 1)
                ow.printLine(0l);
            else if (N == 2)
                ow.printLine(A[1] - A[0]);
            else {
                long sumTillCurrElement = 0l;
                long sumLastElement = 0l;
                long subsetCount = 0;
                for (int j = 1; j < N; j++) {
                    long diff = A[j] - A[j - 1];
                    subsetCount = (subsetCount * 2 + 1) % mod;
                    long currElementSum = (sumLastElement * 2 + subsetCount * diff) % mod;
                    sumTillCurrElement = (currElementSum + sumTillCurrElement) % mod;
                    sumLastElement = currElementSum % mod;
                }
                ow.printLine(sumTillCurrElement);
            }
        }
        ow.close();
    }

}
