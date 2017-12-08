package com.codechef.practice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
 
class CHEFCK {
    
    private static long n, k, q, L, R, mod = 1000000007;
    private static long a, b, c, d, e, f, r, s, t, m, L1, La, Lc, Lm, D1, Da, Dc, Dm;
    private static long[] ar;
    
    public static void main(String[] args) {
        InputReader ir = new InputReader(System.in);
        OutputWriter ow = new OutputWriter(System.out);
        
        n = ir.readLong();
        k = ir.readLong();
        q = ir.readLong();
        a = ir.readLong();
        b = ir.readLong();
        c = ir.readLong();
        d = ir.readLong();
        e = ir.readLong();
        f = ir.readLong();
        r = ir.readLong();
        s = ir.readLong();
        t = ir.readLong();
        m = ir.readLong();
        
        ar = new long[(int)n];
        ar[0] = ir.readLong();
        
        L1 = ir.readLong();
        La = ir.readLong();
        Lc = ir.readLong();
        Lm = ir.readLong();
        D1 = ir.readLong();
        Da = ir.readLong();
        Dc = ir.readLong();
        Dm = ir.readLong();
        
        format_ar();
        
        long sum = 0, prod = 1;
        
        SegmentTree root = new SegmentTree();
        STNode tree = root.constructSegmentTree(ar, 0, ar.length-1);
        for(int i=0; i < q; i++) {
            generate_LR();
            long min;
            if (L == R) {
                min = ar[(int)(L - 1)];
            } else {
                if (L > R) {
                    long temp = L;
                    L = R;
                    R = temp;
                }
                min = root.getMin(tree, L - 1, R - 1);
            }
            
            sum = sum + min;
            prod = (prod * min) % mod;
        }
        ow.printLine(sum +" "+ prod);
        ow.close();
    }
    
    private static void format_ar() {
        long temp = t;
        
        for(int i = 1; i < n; i++) {
            temp = (temp * t) % s;
            if(temp <= r) {
                ar[i] = (int)((((a * ar[i-1] +b)%m) * ar[i-1] + c) % m);
            } else {
                ar[i] = (int)((((d * ar[i-1] +e)%m) * ar[i-1] + f) % m); 
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
 
    static class SegmentTree {
        
        STNode constructSegmentTree(long[] A, long l, long r) {
            if (l == r) {
                STNode node = new STNode();
                node.leftIndex = l;
                node.rightIndex = r;
                node.min = A[(int)l];
                return node;
            }
            long mid = (l + r) / 2;
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
     
        long getMin(STNode root, long l, long r) {
            if (root.leftIndex >= l && root.rightIndex <= r) {
                return root.min;
            }
            if (root.rightIndex < l || root.leftIndex > r) {
                return Integer.MAX_VALUE;
            }
            return Math.min(getMin(root.leftNode, l, r),getMin(root.rightNode, l, r));
        }
     
    }
     
    static class STNode {
        long leftIndex;
     
        long rightIndex;
     
        long min;
     
        STNode leftNode;
     
        STNode rightNode;
    }
}