package com.hiring.softwareAG;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class SplendidMatrices {
	
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
			writer.print(string);
		}

		public void close() {
			writer.flush();
			writer.close();
		}

		public void printLine(long i) {
			writer.print(i);
		}
	}

	public static void main(String[] args) {
		InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		int n =   ir.readInt();
		int matrixLength = 1<<n;
		int[][] matrix = new int[matrixLength+1][matrixLength+1];
		generateSplendidMatrix(matrix, 1, 1, matrixLength,
				matrixLength, 1);
		for(int i=1;i<matrixLength+1;i++) {
			for(int j=1;j<matrixLength+1;j++) {
				ow.printLine(matrix[i][j] + " ");
			}
			ow.printLine("\n");
		}
		ow.close();
	}

	private static void generateSplendidMatrix(int[][] matrix, int startRow,
			int startCol, int endRow, int endCol, int startElement) {
		if ((endRow - startRow == 1) && (endCol - startCol == 1)) {
			matrix[startRow][startCol] = startElement++;
			matrix[startRow][endCol] = startElement++;
			matrix[endRow][startCol] = startElement++;
			matrix[endRow][endCol] = startElement++;
		}else{
			int n = endRow - startRow + 1;
			generateSplendidMatrix(matrix, startRow, startCol, startRow+n/2-1, startCol+n/2-1, startElement);
			generateSplendidMatrix(matrix, startRow, startCol+n/2, startRow+n/2-1, endCol, matrix[startRow+n/2-1][startCol+n/2-1]+1);
			generateSplendidMatrix(matrix, startRow+n/2, startCol, endRow, startCol+n/2-1, matrix[startRow+n/2-1][endCol]+1);
			generateSplendidMatrix(matrix, startRow+n/2, startCol+n/2, endRow, endCol, matrix[endRow][startCol+n/2-1]+1);
		}
	}
	
}
