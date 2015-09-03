package com.hiring.newshunt;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class MergeArray {
	
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

	public static void main(String[] args) {
		InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		int n1 = ir.readInt();
		int[] a = new int[n1];
		for(int i=0;i<n1;i++){
			a[i] = ir.readInt();
		}
		int n2 = ir.readInt();
		int[] b = new int[n2];
		for (int i = 0; i < n2; i++) {
			b[i] = ir.readInt();
		}
		String res = mergeArray(a,b);
		ow.printLine(res);
		ow.close();
	}

	private static String mergeArray(int[] a, int[] b) {
		int[] c = new int[a.length+b.length];
		int i=0,j=0,k=0;
		while(i<a.length&& j<b.length){
			if(a[i]<b[j]){
				c[k++] = a[i];
				i++;
			}else{
				c[k++] = b[j];
				j++;
			}
		}
		while(i<a.length){
			c[k++] = a[i++];
		}
		while(j<b.length){
			c[k++] = b[j++];
		}
		StringBuilder sb = new StringBuilder();
		for(i=0;i<c.length;i++) {
			sb.append(c[i]+" ");
		}
		return sb.toString();
	}
	
}
