package com.codechef;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

class SegmentTree {
	 
    STNode constructSegmentTree(int[] A, int l, int r) {
        if (l == r) {
            STNode node = new STNode();
            node.leftIndex = l;
            node.rightIndex = r;
            node.min = A[l];
            return node;
        }
        int mid = (l + r) / 2;
        STNode leftNode = constructSegmentTree(A, l, mid);
        STNode rightNode = constructSegmentTree(A, mid+1, r);
        STNode root = new STNode();
        root.leftIndex = leftNode.leftIndex;
        root.rightIndex = rightNode.rightIndex;
        root.min = Math.min(leftNode.min,rightNode.min);
        root.leftNode = leftNode;
        root.rightNode = rightNode;
        return root;
    }
 
    int getMin(STNode root, int l, int r) {
    	int lo = Math.min(l,  r);
    	int ro = Math.max(l, r);
    	if (root.rightIndex < ro || root.leftIndex > lo) {
            return Integer.MAX_VALUE;
        }
    	
//    	if(root.leftIndex == root.rightIndex)
//    		return root.min;
    	
    	if (root.leftIndex == lo && root.rightIndex == ro) {
            return root.min;
        }
        
        int mid = (root.leftIndex+root.rightIndex)>>>1;
        return Math.min(getMin(root.leftNode, lo, mid),getMin(root.rightNode, mid+1, ro));
    }
 
    /**
     * @param root
     * @param index index of number to be updated in original array
     * @param newValue
     * @return difference between new and old values
     */
    int updateValueAtIndex(STNode root, int index) {
        int diff = 0;
        if (root.leftIndex == root.rightIndex && index == root.leftIndex) {
            // We actually reached to the leaf node to be updated
            int newValue = root.min^1;
            diff = newValue - root.min;
            root.min = newValue;
            return diff;
        }
        int mid = (root.leftIndex + root.rightIndex) / 2;
        if (index <= mid) {
            diff = updateValueAtIndex(root.leftNode, index);
        } else {
            diff = updateValueAtIndex(root.rightNode, index);
        }
        root.min += diff;
        return diff;
    }
}

class STNode {
    int leftIndex;
 
    int rightIndex;
 
    int min;
 
    STNode leftNode;
 
    STNode rightNode;
}

class CHEFCK {
	
	private static int n, k, q, L, R, mod = 1000000007;
	private static long a, b, c, d, e, f, r, s, t, m, L1, La, Lc, Lm, D1, Da, Dc, Dm;
	private static int[] ar;
	
    public static void main(String[] args) {
    	InputReader ir = new InputReader(System.in);
		OutputWriter ow = new OutputWriter(System.out);
		
		n = ir.readInt();
		k = ir.readInt();
		q = ir.readInt();
		a = ir.readInt();
		b = ir.readInt();
		c = ir.readInt();
		d = ir.readInt();
		e = ir.readInt();
		f = ir.readInt();
		r = ir.readInt();
		s = ir.readInt();
		t = ir.readInt();
		m = ir.readInt();
		
		ar = new int[n];
		ar[0] = ir.readInt();
		
		L1 = ir.readInt();
		La = ir.readInt();
		Lc = ir.readInt();
		Lm = ir.readInt();
		D1 = ir.readInt();
		Da = ir.readInt();
		Dc = ir.readInt();
		Dm = ir.readInt();
		
		format_ar();
		
		long sum = 0, prod = 1;
		
		SegmentTree root = new SegmentTree();
		STNode tree = root.constructSegmentTree(ar, 0, ar.length-1);
		for(int i=0; i < q; i++) {
			generate_LR();
			int min = ar[L-1];
			if (L!=R)	
				min = root.getMin(tree, L-1, R-1);
			sum = (sum + min) % mod;
			prod = (prod * min) % mod;
		}
		ow.printLine(sum +" "+ prod);
		ow.close();
		/**
		 * write the main code here. the array to be used is ar,
		 * the index to retrieve the min value ar L and R
		 */
	}
    
    private static void format_ar() {
    	long temp = t;
    	
    	for(int i = 1; i < n; i++) {
    		temp = (temp * t) % s;
    		if(temp <= r) {
    			ar[i] = (int)((((a * ar[i-1]) % m) * ar[i-1] % m + (b * ar[i-1]) % m + c) % m);
    		} else {
    			ar[i] = (int)((((d * ar[i-1]) % m) * ar[i-1] % m + (e * ar[i-1]) % m + f) % m); 
    		}
    	}
    }
    
    private static void generate_LR() {
    	L1 = (La * L1 + Lc) % Lm;
    	D1 = (Da * D1 + Dc) % Dm;
    	L = (int)(L1 + 1);
    	R = (int)(Math.min(L + k - 1 + D1, n));
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

}

