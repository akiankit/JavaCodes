package com.codeforces;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class A {

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
		int numLines = ir.readInt();
		int[][] cars = new int[numLines][numLines];
		boolean[] collided = new boolean[numLines];
		int count = 0;
		for(int i=0;i<numLines;i++) {
			for(int j=0;j<numLines;j++) {
				cars[i][j] = ir.readInt();
				switch (cars[i][j]) {
				case -1:
				case 0:
					break;
				case 1:
					if(collided[i] == false) {
						count++;
						collided[i]  = true;
					}
					break;
				case 2:
					if (collided[j] == false) {
						count++;
						collided[j] = true;
					}
					break;
				case 3:
					if(collided[i] == false) {
						count++;
						collided[i]  = true;
					}
					if(collided[j] == false) {
						count++;
						collided[j]  = true;
					}
					break;
				}
			}
		}
		count = numLines - count;
		ow.printLine(count);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<numLines;i++) {
			if(collided[i] == false) {
				sb.append((i+1) +" ");
			}
		}
		if(count != 0) {
			ow.printLine(sb.toString());
		}
		ow.close();
	}
}
