package com.hiring.softwareAG;

import java.util.Arrays;
import java.util.Scanner;

public class CrazyPainter {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int tests = scanner.nextInt();
		for (int i = 0; i < tests; i++) {
			long n = scanner.nextInt();
			long[] costs = new long[26];
			long costForOneCycle = 0;
			for (int j = 0; j < 26; j++) {
				costs[j] = scanner.nextLong();
				costForOneCycle += costs[j];
			}
			System.out.println(Arrays.toString(costs));
			System.out.println(costForOneCycle);
			long totalCost = getTotalCost(costs, costForOneCycle, n);
			System.out.println(totalCost);
		}
		scanner.close();
	}

	private static long getTotalCost(long[] costs, long costForOneCycle, long n) {
		long totalCost = 0;
		long numberOfCycles = ((12 * n) / 26);
//		if(numberOfCycles != 0)
			totalCost = (costForOneCycle * (numberOfCycles + (numberOfCycles - 1) * 13));
		long remaining = (12 * n) % 26;
//		System.out.println(totalCost);
		for(int i=0;i<remaining;i++){
			totalCost = totalCost + numberOfCycles + costs[i];
		}
		return totalCost;
	}
}
