package com.hackerearth.challenges.jan18;

import java.util.Arrays;
import java.util.Scanner;

import com.initial.util.ArraysUtil;

public class Buying_items {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		String[] words = line.split(" ");
		int n = Integer.parseInt(words[0]);
		int m = Integer.parseInt(words[1]);
		int[][] objects = new int[m][n];
		int[] costs = new int[m];
		for (int i = 0; i < m; i++) {
			line = sc.nextLine();
			words = line.split(" ");
			int j = 0;
			for (; j < n; j++) {
				objects[i][j] = Integer.parseInt(words[j]);
			}
			costs[i] = Integer.parseInt(words[j]);
		}
		sc.close();
		ArraysUtil.print2DArray(objects);
		ArraysUtil.print1DArray(costs);
		System.out.println(getMinCost(objects, costs, n, m));

	}

	public static int getMinCost(int[][] objects, int[] costs, int n, int m) {
		int[] expected = new int[n];
		for (int i = 0; i < expected.length; i++) {
			expected[i] = 1;
		}
		float totalCost = 0;
		int[] current = new int[n];
		while (!Arrays.equals(current, expected)) {
			int index = -1;
			int maxDiff = Integer.MIN_VALUE;
			float minCost = Integer.MAX_VALUE;
			for (int i = 0; i < m; i++) {
				int diff = getDiffOfArrays(objects[i], current);
				if (diff == 0) {
					continue;
				}
				float cost = costs[i] / (float) diff;
				if (cost == minCost && diff > maxDiff) {
					minCost = costs[i];
					index = i;
				} else if (cost < minCost) {
					minCost = costs[i];
					index = i;
				}
			}
			if (index != -1) {
				unionArrays(objects[index], current);
			}
			totalCost = totalCost + minCost;
		}
		return (int) totalCost;
	}

	private static void unionArrays(int[] set, int[] currentUnion) {
		for (int i = 0; i < set.length; i++) {
			if (currentUnion[i] == 0 && set[i] != 0) {
				currentUnion[i] = 1;
			}
		}
	}

	private static int getDiffOfArrays(int[] set, int[] currentUnion) {
		int diff = 0;
		for (int i = 0; i < set.length; i++) {
			if (set[i] != 0 && currentUnion[i] == 0) {
				diff++;
			}
		}
		return diff;
	}

}
