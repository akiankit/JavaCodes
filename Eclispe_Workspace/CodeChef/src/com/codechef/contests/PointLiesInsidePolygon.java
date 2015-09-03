package com.codechef.contests;

public class PointLiesInsidePolygon {

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

	public static boolean isCollinear(Point p, Point q, Point r) {
		return getOrientation(p, q, r) == 0;
	}
	
	//(y2-y1)(x3-x2)-(y3-y2)(x2-x1)
	public static int getOrientation(Point p, Point q, Point r) {
		int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
		if(val == 0)
			return 0; // Colinear
		return val < 0 ? -1 : 1;// 1 means clockwise
	}
	
	public static boolean isPointInsidePolygon(Polygon polygon, Point point) {
		
		LineSegment infiniteLine = makeInfiniteLine(point);
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
		return (count & 1) == 1;
	}

	private static LineSegment makeInfiniteLine(Point point) {
		return new LineSegment(point, new Point(Integer.MAX_VALUE, point.y));
	}
	
	public static void main(String[] args) {
		Point[] points = new Point[4];
		points[0] = new Point(0, 0);
		points[1] = new Point(10, 0);
		points[2] = new Point(10, 10);
		points[3] = new Point(0, 10);
		Polygon polygon = new Polygon(points);
		System.out.println("For square");
		Point p = new Point(20,20);
		System.out.println(isPointInsidePolygon(polygon, p));
		p = new Point(5,5);
		System.out.println(isPointInsidePolygon(polygon, p));
		p = new Point(-1,10);
		System.out.println(isPointInsidePolygon(polygon, p));
		System.out.println("For traingle");
		points = new Point[3];
		points[0] = new Point(0, 0);
		points[1] = new Point(5, 5);
		points[2] = new Point(5, 0);
		polygon = new Polygon(points);
		p = new Point(3,3);
		System.out.println(isPointInsidePolygon(polygon, p));
		p = new Point(5,1);
		System.out.println(isPointInsidePolygon(polygon, p));
		p = new Point(8,1);
		System.out.println(isPointInsidePolygon(polygon, p));
		/**/
	}
}
