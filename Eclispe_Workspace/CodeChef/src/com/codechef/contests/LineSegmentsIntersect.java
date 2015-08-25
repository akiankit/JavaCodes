package com.codechef.contests;

public class LineSegmentsIntersect {

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
		if (o1 == 0 && isOnSegment(ls1, p2))
			return true;
		if (o2 == 0 && isOnSegment(ls1, q2))
			return true;
		if (o3 == 0 && isOnSegment(ls2, p1))
			return true;
		if (o4 == 0 && isOnSegment(ls2, q1))
			return true;
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

	//(y2-y1)(x3-x2)-(y3-y2)(x2-x1)
	public static int getOrientation(Point p, Point q, Point r) {
		int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
		if(val == 0)
			return 0; // Colinear
		return val < 0 ? -1 : 1;// 1 means clockwise
	}
	
	public static void main(String[] args) {
		Point p1 = new Point(1,1);
		Point q1 = new Point(10,1);
		Point p2 = new Point(1,2);
		Point q2 = new Point(10,2);
		LineSegment ls1 = new LineSegment(p1, q1);
		LineSegment ls2 = new LineSegment(p2, q2);
		System.out.println(isIntersect(ls1, ls2));
		int[] p1ar = { 10, 0 };
		int[] p2ar = { 0, 10 };
		ls1 = new LineSegment(p1ar, p2ar);
		p1ar = new int[] {0,0};
		p2ar = new int[] {10,10};
		ls2 = new LineSegment(p1ar, p2ar);
		System.out.println(isIntersect(ls1, ls2));
	}
}

