package com.misc;

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
	
	public static void main(String[] args) {
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
	}
}

class SegmentTreeNode {
	int start, end;
	long val;
	SegmentTreeNode left, right;
}

class SumSegmentTreeSelfWritten {
	
	long prime = 1000000007;

	SegmentTreeNode root = null;

	public SegmentTreeNode createSegmentTree(long[] arr) {
		root = createSegmentTreeUtil(arr, 0, arr.length - 1);
		return root;
	}

	private SegmentTreeNode createSegmentTreeUtil(long[] arr, int s, int e) {
		if (s == e) {
			SegmentTreeNode node = new SegmentTreeNode();
			node.start = s;
			node.end = e;
			node.val = arr[s];
			return node;
		}
		int mid = (s + e) / 2;
		SegmentTreeNode node = new SegmentTreeNode();
		node.start = s;
		node.end = e;
		node.left = createSegmentTreeUtil(arr, s, mid);
		node.right = createSegmentTreeUtil(arr, mid + 1, e);
		node.val = (node.left.val + node.right.val) % prime;
		return node;
	}

	public long query(SegmentTreeNode root, int start, int end) {
		if (root.start > end || root.end < start)
			return 0;
		else if (start <= root.start && end >= root.end)
			return root.val;
		int mid = (root.start + root.end) / 2;
		if (end <= mid)
			return query(root.left, start, end);
		else if (start > mid)
			return query(root.right, start, end);
		else
			return (query(root.left, start, mid)
					+ query(root.right, mid + 1, end))%prime;

	}
	
	public void modify(SegmentTreeNode root, int index, long value) {
        Stack<SegmentTreeNode> stack = new Stack<SegmentTreeNode>();
        boolean isUpdated = false;
        SegmentTreeNode node = root;
        while(!isUpdated){
            if (index == node.start && index == node.end) {
                node.val = (node.val + value)%prime;
                isUpdated = true;
                break;
            }
            stack.push(node);
            int mid = (node.start + node.end)/2;
            if(index <= mid)
                node = node.left;
            else 
                node = node.right;
        }
        while(!stack.isEmpty()) {
            node = stack.pop();
            node.val = (node.left.val + node.right.val)%prime;
        }
	}
}