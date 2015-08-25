package com.codechef.contests;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class ANKLEX {

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
	
	private static int base = 29, mod = 1000000007;
	private static long[][] hash, revHash;
	private static int[] charTodigi;
	static {
		charTodigi = new int[256];
		for(int i = 'a'; i <= 'z'; i++)
			charTodigi[i] = i - 'a';
	}
	
	private static void process(String s) {
		int r = 2, half = 1;
		hash = new long[s.length()-1][];
		hash[1] = new long[s.length()];
		for(int i = 0; i < s.length(); i++)
			hash[1][i] = charTodigi[s.charAt(i)];
		
		long multiplier = base;
		while(r < s.length()) {
			hash[r] = new long[s.length() - half];
			for(int i = 0; i < hash[r].length; i++)
				hash[r][i] = (hash[half][i] * multiplier + hash[half][i + half]) % mod;
			
			r = r << 1;
			half = half << 1;
			multiplier = (multiplier * multiplier) % mod;
		}
		
		r = 2;
		half = 1;
		multiplier = base;
		revHash = new long[s.length() - 1][];
		revHash[1] = hash[1];
		while(r < s.length()) {
			revHash[r] = new long[s.length() - half];
			for(int i = hash[r].length - 1; i >= 0; i--)
				revHash[r][i] = (revHash[half][i] + multiplier * revHash[half][i + half]) % mod;
			
			r = r << 1;
			half = half << 1;
			multiplier = (multiplier * multiplier) % mod;
		}
	}
	
	/**
	 * returns hash value of substring of s from index i to index j both inclusive.
	 * @param i start index of substring in main string. index is zero based index.
	 * @param j end index of the substring.
	 * @return returns hash value of substring from index i to index j.
	 */
	private static long getHash(int i, int j) {
		int index = Integer.highestOneBit(j + 1 - i);
		long hash = 0;
		while(index > 0) {
			
		}
		return 0;
	}
	
	public static void main(String[] args) {
		InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		String input = ir.next();
		process(input);
		int queries = ir.readInt();
		for (int i = 0; i < queries; i++) {
			int index = ir.readInt();
			int length = ir.readInt();
			int possibleForLength = input.length() + 1 - length;
			System.out.println(possibleForLength);
			if (possibleForLength <= index)
				ow.printLine("-1");
			else
				ow.printLine(index + 1);
		}
		ow.close();
	}
}
