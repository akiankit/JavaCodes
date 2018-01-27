package com.hackerrank.misc.problems;

import java.util.Scanner;

/**
 * <a href="https://www.hackerrank.com/challenges/queens-attack-2/problem">
 * Problem Link</a> <br>
 * <br>
 */
public class QueensAttackII {

	static int[][] maxPoints = new int[8][2];
	static int row;
	static int col;

	static void initMaxPoints(int n, int r, int c) {
		row = r;
		col = c;
		maxPoints[0] = new int[] { r, 1 }; // left most in same row
		// Left Top point r++, c-- max Point: (n,1)
		int diff = Math.min(n - r, c - 1);
		maxPoints[1] = new int[] { r + diff, c - diff };
		maxPoints[2] = new int[] { n, c }; // top in same column
		// Right top point r++, c++ max Point: (n,1)
		diff = Math.min(n - r, n - c);
		maxPoints[3] = new int[] { r + diff, c + diff };
		maxPoints[4] = new int[] { r, n }; // right most in same row
		// Right bottom point r--, c++ max Point: (n,1)
		diff = Math.min(r - 1, n - c);
		maxPoints[5] = new int[] { r - diff, c + diff };
		maxPoints[6] = new int[] { 1, c }; // bottom in same column
		// left bottom point r--, c-- max Point: (n,1)
		diff = Math.min(r - 1, c - 1);
		maxPoints[7] = new int[] { r - diff, c - diff };
		print2DArray(maxPoints);
	}

	static int getDirectionForObstacle(int[] obstacle) {
		int r = obstacle[0];
		int c = obstacle[1];
		// in same row
		if (r == row) {
			return c - col > 0 ? 4 : 0;
		}
		// In same col
		if (c == col) {
			return r - row > 0 ? 2 : 6;
		}
		if (Math.abs(r - row) != Math.abs(c - col)) {
			return -1;
		}
		// Right top
		if (r > row && c > col) {
			return 3;
		}
		// Left top
		if (r > row && c < col) {
			return 1;
		}
		// Right bottom
		if (r < row && c > col) {
			return 5;
		}
		// Left bottom
		if (r < row && c < col) {
			return 7;
		}
		return -1;
	}

	static void updateMaxPoint(int direction, int[] obstacle) {
		int[] currentMax = maxPoints[direction];
		if (direction == 0) {
			currentMax[1] = Math.max(obstacle[1] + 1, currentMax[1]);
		}
		if (direction == 4) {
			currentMax[1] = Math.min(obstacle[1] - 1, currentMax[1]);
		}
		if (direction == 2) {
			currentMax[0] = Math.min(obstacle[0] - 1, currentMax[0]);
		}
		if (direction == 6) {
			currentMax[0] = Math.max(obstacle[0] + 1, currentMax[0]);
		}
		if (direction == 1) {
			currentMax[0] = Math.min(obstacle[0] - 1, currentMax[0]);
			currentMax[1] = Math.max(obstacle[1] + 1, currentMax[1]);
		}
		if (direction == 3) {
			currentMax[0] = Math.min(obstacle[0] - 1, currentMax[0]);
			currentMax[1] = Math.min(obstacle[1] - 1, currentMax[1]);
		}
		if (direction == 5) {
			currentMax[0] = Math.max(obstacle[0] + 1, currentMax[0]);
			currentMax[1] = Math.max(obstacle[1] + 1, currentMax[1]);
		}
		if (direction == 7) {
			currentMax[0] = Math.max(obstacle[0] + 1, currentMax[0]);
			currentMax[1] = Math.min(obstacle[1] - 1, currentMax[1]);
		}
	}

	static int countAllPointsToAttack() {
		int count = 0;
		count += Math.abs(maxPoints[0][1] - col);
		count += Math.abs(maxPoints[1][0] - row);
		count += Math.abs(maxPoints[2][0] - row);
		count += Math.abs(maxPoints[3][1] - col);
		count += Math.abs(maxPoints[4][1] - col);
		count += Math.abs(maxPoints[5][1] - col);
		count += Math.abs(row - maxPoints[6][0]);
		count += Math.abs(row - maxPoints[7][0]);
		return count;
	}

	static int queensAttack(int n, int k, int r, int c, int[][] obstacles) {
		initMaxPoints(n, r, c);
		for (int i = 0; i < obstacles.length; i++) {
			int[] obstacle = obstacles[i];
			int direction = getDirectionForObstacle(obstacle);
			if (direction == -1) {
				continue;
			}
			updateMaxPoint(direction, obstacle);
		}
		System.out.println("===========================");
		print2DArray(maxPoints);
		return countAllPointsToAttack();
	}

	public static void print2DArray(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			// System.out.println(Arrays.toString(array[i]));
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int k = in.nextInt();
		int r_q = in.nextInt();
		int c_q = in.nextInt();
		int[][] obstacles = new int[k][2];
		for (int obstacles_i = 0; obstacles_i < k; obstacles_i++) {
			for (int obstacles_j = 0; obstacles_j < 2; obstacles_j++) {
				obstacles[obstacles_i][obstacles_j] = in.nextInt();
			}
		}
		int result = queensAttack(n, k, r_q, c_q, obstacles);
		System.out.println(result);
		in.close();
	}
}
