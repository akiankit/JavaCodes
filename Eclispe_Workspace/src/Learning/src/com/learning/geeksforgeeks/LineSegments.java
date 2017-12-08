package com.learning.geeksforgeeks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

public class LineSegments {
	
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
//		int[] A = {2,1,1,3};
//		int[] B= {5,4,6,8};
		InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		int num = ir.readInt();
		LineSegmentTree tree = new LineSegmentTree();
		for(int i=0;i<num;i++) {
			tree.addVal(ir.readInt(), ir.readInt());
		}
		
//		for(int i=0;i<4;i++) {
//			tree.addVal(A[i], B[i]);
//		}
		System.out.println(tree.getLongestSubset(tree.root));
		ow.close();
	}
	
}
class LineSegmentTree {
	 
    LineSegmentNode root;
 
    public void addVal(int start, int end) {
//        root = addNode(root,new LineSegmentNode(start, end));
    }
    
    private static LineSegmentNode RotateLL(LineSegmentNode root) {
    	LineSegmentNode left = root.left;
        root.left = left.right;
        left.right = root;
        root.height =
                Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        left.height = Math.max(getHeight(left.left), root.height) + 1;
        return left;
    }

    private static LineSegmentNode RotateLR(LineSegmentNode root) {
        root.left = RotateRR(root.left);
        return RotateLL(root);
    }

    private static LineSegmentNode RotateRL(LineSegmentNode root) {
        root.right = RotateLL(root.right);
        return RotateRR(root);
    }

    private static LineSegmentNode RotateRR(LineSegmentNode root) {
    	LineSegmentNode right = root.right;
        root.right = right.left;
        right.left = root;
        root.height =
                Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        right.height = Math.max(root.height, getHeight(right.right)) + 1;
        return right;
    }

    private static int getHeight(LineSegmentNode root) {
        if (root == null)
            return 0;
        return root.height;
    }
    
    /*private LineSegmentNode addNode(LineSegmentNode root, LineSegmentNode node) {
        if (root == null) {
            root = node;
            return root;
        }
        if (isBadPoint(root,node)) {
            root.right = addNode(root.right, node);
            if (root.right.height - getHeight(root.left) == 2) {
                if (isBadPoint(root.right, node))
                    root = RotateRR(root);
                else
                    root = RotateRL(root);
            }
            root.height =
                    Math.max(getHeight(root.left), getHeight(root.right)) + 1;
            return root;
        }

        root.left = addNode(root.left, node);
        if (root.left.height - getHeight(root.right) == 2) {
            if (isBadPoint(root.left, node))
                root = RotateLR(root);
            else
                root = RotateLL(root);
        }
        root.height =
                Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        return root;
    }*/
 
    private boolean isBadPoint(LineSegmentNode temp, LineSegmentNode node) {
    	if(node.start <= temp.start && node.end >= temp.end)
    		return true;
    	return false;
	}

    public int getLongestSubset(LineSegmentNode node) {
    	if(node == null)
    		return 0;
    	if(node.right == null){
    		int count = 0;
    		LineSegmentNode temp = node;
        	while(temp != null) {
        		count++;
        		temp = temp.left;
        	}
        	return count;
    	}
    	return Math.max(getLongestSubset(node.left)+1, getLongestSubset(node.right));
    }
 
    @Override
    public String toString() {
        return root.toString();
    }
}

class LineSegmentNode {
	LineSegmentNode left, right;
 
    int start,end;
    int height;
 
    public LineSegmentNode(int start, int end) {
        this.start = start;
        this.end = end;
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (left != null) {
            sb.append("left:(" + left.start +","+left.end+")");
        } else {
            sb.append("left=null");
        }
        sb.append(",val:(" +this.start+","+this.end+")" );
        if (right != null) {
            sb.append(",right:(" + right.start+","+right.end+")");
        } else {
            sb.append(",right=null");
        }
        return sb.toString();
    }
}