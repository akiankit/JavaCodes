package com.hackerrank.misc.problems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * <a href="https://www.hackerrank.com/challenges/two-pluses/problem"> Problem
 * Link</a> <br>
 * <br>
 * Idea is pretty simple(Kind of brute force):<br>
 * 
 * <pre>
 * Get all pluses,
 * Store all points contained in a list.
 * iterate on list and calculate maximal product of plus's area if non-overlapping
 * </pre>
 */
public class TwoPluses {

	static char GOOD_CHAR = 'G';
	static char BAD_CHAR = 'B';
	static Set<Plus> pluses = new HashSet<>();
	static char[][] mainGrid = null;
	static int row;
	static int col;

	static void initGrid(String[] grid2) {
		for (int i = 0; i < grid2.length; i++) {
			String row = grid2[i];
			for (int j = 0; j < row.length(); j++) {
				mainGrid[i][j] = row.charAt(j);
			}
		}
	}

	static int twoPluses(String[] grid) {
		initGrid(grid);
		initPluses(grid);
		for (int i = 0; i < pluses.size(); i++) {
			// System.out.println(pluses.get(i));
		}
		// Collections.sort(pluses);
		// System.out.println("======================================");
		return getMaximalProductOfNonOverlappingPluses();
		// Get all pluses
		// Sort them in desc order of areas
		// Point of sorting is to use binary search
		// for each plus find out plux with max area which is not overlapping
		// from current plus
	}

	private static int getMaximalProductOfNonOverlappingPluses() {
		int max = 1;
		List<Plus> plusesList = new ArrayList<>(pluses.size());
		plusesList.addAll(pluses);
		for (int i = 0; i < plusesList.size(); i++) {
			Plus plus1 = plusesList.get(i);
			for (int j = i + 1; j < plusesList.size(); j++) {
				Plus plus2 = plusesList.get(j);
				if (!plus1.overlaps(plus2)) {
					max = Math.max(max, plus1.getArea() * plus2.getArea());
				}
			}
		}
		return max;
	}

	private static void initPluses(String[] grid) {
		for (int i = 0; i < grid.length; i++) {
			String row = grid[i];
			for (int j = 0; j < row.length(); j++) {
				if (i == 3 && j == 4) {
					// System.out.println("aldjflkadj");
				}
				if (row.charAt(j) == GOOD_CHAR) {
					getAreaForMaxPlusWithCenter(i, j);
				}
			}
		}
	}

	private static void getAreaForMaxPlusWithCenter(int i, int j) {
		Plus plus = new Plus(i, j);
		pluses.add(plus);
		int area = 1;
		int next = 1;
		while (canGrow(i, j, next)) {
			area += 4;
			plus.addCoordinate(next);
			plus.setArea(area);
			pluses.add(new Plus(plus));
			next++;
		}
		pluses.add(plus);
	}

	private static boolean canGrow(int i, int j, int k) {
		boolean bottomValid = i + k < row && mainGrid[i + k][j] == GOOD_CHAR;
		if (!bottomValid) {
			return false;
		}
		boolean topValid = i - k >= 0 && mainGrid[i - k][j] == GOOD_CHAR;
		if (!topValid) {
			return false;
		}
		boolean leftValid = j - k >= 0 && mainGrid[i][j - k] == GOOD_CHAR;
		if (!leftValid) {
			return false;
		}
		boolean rightValid = j + k < col && mainGrid[i][j + k] == GOOD_CHAR;
		if (!rightValid) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int m = in.nextInt();
		row = n;
		col = m;
		mainGrid = new char[n][m];
		String[] grid = new String[n];
		for (int grid_i = 0; grid_i < n; grid_i++) {
			grid[grid_i] = in.next();
		}
		int result = twoPluses(grid);
		System.out.println(result);
		in.close();
	}
}

class Coordinate implements Comparable<Coordinate> {

	int x;
	int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordinate [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int compareTo(Coordinate o) {
		if (x != o.x) {
			return x - o.x;
		}
		return y - o.y;
	}

	@Override
	public int hashCode() {
		return x * y;
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate o = (Coordinate) obj;
		return x == o.x && y == o.y;
	}

}

class Plus implements Comparable<Plus> {

	private Set<Coordinate> points = new HashSet<>();
	private int area = 1;
	Coordinate center;

	public Plus(Coordinate center) {
		this.center = center;
	}

	public Plus(int x, int y) {
		this.center = new Coordinate(x, y);
		getPoints().add(center);
	}

	public Plus(Plus plus) {
		this.area = plus.area;
		this.center = plus.center;
		this.points.addAll(plus.getPoints());
	}

	public void increaseArea() {
		setArea(getArea() + 4);
	}

	public void addCoordinate(int k) {
		getPoints().add(new Coordinate(center.x + k, center.y));
		getPoints().add(new Coordinate(center.x - k, center.y));
		getPoints().add(new Coordinate(center.x, center.y + k));
		getPoints().add(new Coordinate(center.x, center.y - k));
	}

	@Override
	public String toString() {
		return "Plus [points=" + points + ", area=" + area + ", center=" + center + "]";
	}

	/**
	 * @return the area
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}

	@Override
	public int compareTo(Plus o) {
		if (area != o.area) {
			return area - o.area;
		}
		return center.compareTo(o.center);
	}

	public boolean overlaps(Plus plus) {
		Set<Coordinate> points1 = getPoints();
		Set<Coordinate> points2 = plus.getPoints();
		for (Coordinate point : points1) {
			if (points2.contains(point)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the points
	 */
	public Set<Coordinate> getPoints() {
		return points;
	}

	/**
	 * @param points
	 *            the points to set
	 */
	public void setPoints(Set<Coordinate> points) {
		this.points = points;
	}
}
