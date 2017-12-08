package com.learning.geeksforgeeks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class PALIN {
	
	private static long[] hash, revHash;
	private static int[] charToNum;
	private static int base = 29, mod = 1000000007;
	
	static {
		charToNum = new int[256];
		for(int i = 'a'; i <= 'z'; i++)
			charToNum[i] = i - 'a';
	}
	
	public static void main(String[] args) {
		InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		String yes = "Yes\n", no = "No\n";
		process(ir.next());
		int q = ir.readInt();
		StringBuilder sb = new StringBuilder(q << 2);
		while(q > 0) {
			q--;
			if(checkPalin(ir.readInt(), ir.readInt(), ir.readInt(), ir.readInt()))
				sb.append(yes);
			else
				sb.append(no);
		}
		ow.printLine(sb.toString());
	}
	
	private static boolean checkPalin(int i, int j, int k, int l) {
		if(k <= i && l >= j)
			return caseKIJL(i, j, k, l);
		return true;
	}
	
	private static boolean caseKIJL(int i, int j, int k, int l) {
		long hash1;
		return false;
	}
	
	private static void process(String s) {
		hash = new long[s.length()];
		revHash = new long[s.length()];
		hash[0] = charToNum[s.charAt(0)];
		revHash[s.length() - 1] = charToNum[s.charAt(s.length() - 1)];
		
		for(int i = 1; i < s.length(); i++)
			hash[i] = (hash[i] * base + charToNum[s.charAt(i)]) % mod;
		
		for(int i = s.length() - 2; i >= 0; i++)
			revHash[i] = (revHash[i + 1] * base + charToNum[s.charAt(i)]) % mod;
	}
	
	private static long getHash(int i, int j) {
		if(i == 0)
			return hash[j];
		
		long result = (hash[j] - pow(j + 1 - i) * hash[i - 1]) % mod;
		if(result < 0)
			return mod - result;
		return result;
	}
	
	private static long getRevHash(int i, int j) {
		if(j == revHash.length - 1)
			return revHash[i];
		
		long result = (revHash[i] - revHash[j + 1] * pow(j + 1 - i)) % mod;
		if(result < 0)
			return mod - result;
		return result;
	}
	
	private static boolean checkPalin(int i, int j) {
		return getHash(i, j) == getRevHash(i, j);
	}
	
	/**
	 * returns base raised to power i modulo mod.
	 * @param i
	 * @return
	 */
	private static long pow(int i) {
		long res = base;
		int r = Integer.highestOneBit(i);
		while(r > 1) {
			r = r >>> 1;
			res = res * res % mod;
			if((i & r) != 0)
				res = res * base % mod;
		}
		return res;
	}
	
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
	
}
