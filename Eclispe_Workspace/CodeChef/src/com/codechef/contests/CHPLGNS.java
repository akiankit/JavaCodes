package com.codechef.contests;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Stack;

public class CHPLGNS {

	static class Polygon{
		Point[] points;
		
		public Polygon(Point[] points) {
			this.points = points;
		}
	
		public LineSegment[] getLineSegments() {
			LineSegment[] segments = new LineSegment[points.length];
			int i=0;
			for(;i<points.length-1;i++) {
				segments[i] = new LineSegment(points[i], points[i+1]);
			}
			segments[i] = new LineSegment(points[i], points[0]);
			return segments;
		}
	}

	static class LineSegment {
		Point p1;
		Point p2;
		
		public LineSegment(Point p1, Point p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public LineSegment(int[] p1, int[] p2) {
			this.p1 = new Point(p1);
			this.p2 = new Point(p2);
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Point1="+p1+" Point2="+p2;
		}
	}

	static class Point {

		int x;
		int y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		Point(int[] p) {
			this.x = p[0];
			this.y = p[1];
		} 
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "x="+x+" y="+y;
		}
	}
	
	public static boolean isIntersect(LineSegment ls1, LineSegment ls2) {
		Point p1 = ls1.p1;
		Point q1 = ls1.p2;
		Point p2 = ls2.p1;
		Point q2 = ls2.p2;
		int o1 = getOrientation(p1, q1, p2);
		int o2 = getOrientation(p1, q1, q2);
		int o3 = getOrientation(p2, q2, p1);
		int o4 = getOrientation(p2, q2, q1);
		if(o1 != o2 && o3 != o4) {
			return true;
		}
		// Check if points are colinear.
		/*if (o1 == 0 && isOnSegment(ls1, p2))
			return true;
		if (o2 == 0 && isOnSegment(ls1, q2))
			return true;
		if (o3 == 0 && isOnSegment(ls2, p1))
			return true;
		if (o4 == 0 && isOnSegment(ls2, q1))
			return true;*/
		return false;
	}

	// Check if Point r lies on line segment ls1 
	public static boolean isOnSegment(LineSegment ls1, Point r) {
		return isOnSegment(ls1.p1, ls1.p2, r); 
	}
	
	private static boolean isOnSegment(Point p, Point q, Point r) {
		if (r.x <= Math.max(p.x, q.x) && r.x >= Math.min(p.x, q.x)
				&& r.y <= Math.max(p.y, q.y) && r.y >= Math.min(p.y, q.y))
			return true;
		return false;
	}

	public static boolean isCollinear(Point p, Point q, Point r) {
		return getOrientation(p, q, r) == 0;
	}
	
	//(y2-y1)(x3-x2)-(y3-y2)(x2-x1)
	public static int getOrientation(Point p, Point q, Point r) {
		long val = 1L * (q.y - p.y) * (r.x - q.x) - 1L * (q.x - p.x) * (r.y - q.y);
//		double val = getSlope(p, q)- getSlope(q, r);
		if(val == 0)
			return 0;
		return val < 0 ? -1 : 1;// 1 means clockwise
	}
	
	public static double getSlope(Point p1, Point p2) {
		if(p1.x == p2.x)
			return Integer.MAX_VALUE;
		return (p2.y-p1.y)/(p2.x-p1.x);
	}
	
	private static boolean pointInPoly(Polygon polygon, Point point) {
		int clockwise = 0, count_clock = 0;
		Point[] points = polygon.points;
		for(int i = 0; i < points.length - 1; i++) {
			int orientation = getOrientation(points[i], points[i + 1], point);
			if(orientation == 1)
				clockwise++;
			else if(orientation == -1)
				count_clock++;
			if(clockwise > 0 && count_clock > 0)
				return false;
		}
		int orientation = getOrientation(points[points.length - 1], points[0], point);
		if(orientation == 1)
			clockwise++;
		else if(orientation == -1)
			count_clock++;
		
		if(clockwise > 0 && count_clock > 0)
			return false;
		return true;
	}
	
	public static boolean isPointInsidePolygon(Polygon polygon, Point point) {
		return pointInPoly(polygon, point);
		
		/*LineSegment infiniteLine = makeInfiniteLine(point);
		LineSegment[] lineSegments = polygon.getLineSegments();
		int count = 0;
		for (int i = 0; i < lineSegments.length; i++) {
			LineSegment lineSegment = lineSegments[i];
			if (isIntersect(lineSegment, infiniteLine)) {
				if (isCollinear(lineSegment.p1, lineSegment.p2, point)) {
					return isOnSegment(lineSegment, point);
				}
				count++;
			}
		}
		return (count & 1) == 1;*/
	}

	private static LineSegment makeInfiniteLine(Point point) {
		return new LineSegment(point, new Point(Integer.MAX_VALUE, point.y));
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

	public static void main(String[] args) {
		InputReader in = new InputReader(System.in);
		OutputWriter out = new OutputWriter(System.out);
		
		int t = in.readInt();
		while(t > 0) {
			t--;
			int n = in.readInt();
			Polygon[] polygon = new Polygon[n];
			for(int i = 0; i < n; i++) {
				int m = in.readInt();
				Point[] points = new Point[m];
				for(int j = 0; j < m; j++)
					points[j] = new Point(in.readInt(), in.readInt());
				polygon[i] = new Polygon(points);
			}
			StringBuilder sb = new StringBuilder(n << 2);
			for(int e : solve(polygon))
				sb.append(e).append(' ');
			out.printLine(sb.toString());
		}
		
		out.close();
		
	}
	
	/**
	 * 
	 * @param polygon polygon array for which we have to return results
	 * @return ith value is number of polygon inside ith polygon.
	 */
	private static int[] solve(Polygon[] polygon) {
		Node root = new Node(0);
		addPolygons(root, polygon);
		
		int[] inorder = new int[polygon.length];
		process(root, inorder);
		int[] result = new int[polygon.length];
		for(int i = 0; i < result.length; i++) {
			result[inorder[i]] = i;
		}
		return result;
	}
	
	private static void process(Node root, int[] ar) {
		int i = 0;
		Stack<Node> stack = new Stack<Node>();
		while (true) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			if (stack.isEmpty() == true) {
				break;
			}
			root = stack.pop();
			ar[i] = root.data;
			i++;
			root = root.right;
		}
	}
	
	private static void addPolygons(Node root, Polygon[] polygon) {
		for(int i = 1; i < polygon.length; i++) {
			Node temp = root;
			Point point = polygon[i].points[0];
			while(true) {
				if(isPointInsidePolygon(polygon[temp.data], point)) {
					if(temp.left == null) {
						temp.left = new Node(i);
						break;
					}
					temp = temp.left;
				} else {
					if(temp.right == null) {
						temp.right = new Node(i);
						break;
					}
					temp = temp.right;
				}
			}
		}
	}
	
	static class Node {
		Node left, right;
		int data;
		Node(int n) {
			data = n;
		}
	}
}
