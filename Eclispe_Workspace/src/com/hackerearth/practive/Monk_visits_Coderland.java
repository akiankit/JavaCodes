package com.hackerearth.practive;

import java.util.Scanner;

public class Monk_visits_Coderland {

	public static void main(String args[]) throws Exception {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			long n = scanner.nextInt();
			String nextLine = scanner.nextLine();
			nextLine = scanner.nextLine();
			// System.out.println(nextLine);
			String[] split = nextLine.split("\\s");
			// System.out.println(Arrays.toString(split));
			long[] costs = new long[(int) n];
			for (int j = 0; j < split.length; j++) {
				costs[j] = Integer.parseInt(split[j].trim());
			}
			nextLine = scanner.nextLine();
			split = nextLine.split(" ");
			long[] petrol = new long[(int) n];
			for (int j = 0; j < split.length; j++) {
				petrol[j] = Integer.parseInt(split[j].trim());
			}
			// System.out.println(Arrays.toString(costs));
			// System.out.println(Arrays.toString(petrol));
			long cost = getMinPetrolCost(costs, petrol);
			System.out.println(cost);
		}
		scanner.close();
	}

	private static long getMinPetrolCost(long[] costs, long[] petrol) {
		if (costs.length == 1) {
			return costs[0] * petrol[0];
		}
		long totalCost = 0;
		long minCost = costs[0];
		for (int i = 0; i < costs.length; i++) {
			if (minCost > costs[i]) {
				minCost = costs[i];
			}
			totalCost += minCost * petrol[i];
		}
		return totalCost;
	}
}
