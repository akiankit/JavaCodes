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

class MAXCOUNT {

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
	    int testCasesCount = ir.readInt();
	    OutputWriter ow = new OutputWriter(System.out);
		for (int i = 0; i < testCasesCount; i++) {
			int numCount = ir.readInt();
			int num[] = new int[numCount];
			Map<Integer,Integer> counts = new HashMap<Integer,Integer>();
			for(int j=0;j<numCount;j++){
				int nextNum = ir.readInt();
				num[j] = nextNum;
				counts.put(nextNum, 0);
			}
            for(int j=0;j<numCount;j++){
			    counts.put(num[j], counts.get(num[j])+1);
			}
            int maxCount = 1;
            int maxCountNum = num[0];
			for(int j=0;j<numCount;j++){
				if (maxCount < counts.get(num[j])){
				    maxCount = counts.get(num[j]);
				    maxCountNum = num[j];
				} else if(maxCount == counts.get(num[j]) && maxCountNum > num[j]){
				    maxCountNum = num[j];
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(maxCountNum).append(" ").append(maxCount);
			ow.printLine(sb.toString());
		}
		ow.close();
	}

}