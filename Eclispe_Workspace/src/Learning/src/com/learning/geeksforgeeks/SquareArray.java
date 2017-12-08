package com.learning.geeksforgeeks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Stack;

public class SquareArray {

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
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					outputStream)));
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
	
	public static void main(String[] args) {/*
		InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		int n = ir.readInt();
		int q = ir.readInt();
		long[] arr = new long[n];
		SumSegmentTreeSelfWritten tree = new SumSegmentTreeSelfWritten();
		SegmentTreeNode root = tree.createSegmentTree(arr);
		for (int i = 0; i < q; i++) {
			int type = ir.readInt();
			int x = ir.readInt();
			int y = ir.readInt();
			if (type == 2) {
				ow.printLine(tree.query(root, x-1, y-1));
			} else {
				long k = 1;
				for (int j = x - 1; j <= y - 1; j++) {
					tree.modify(root, j, (k) * (k + 1));
					k++;
				}
			}
		}
		ow.close();
	*/}
}

