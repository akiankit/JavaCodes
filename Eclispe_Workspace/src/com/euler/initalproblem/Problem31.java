/*
 * In England the currency is made up of pound, �, and pence, p, and there are
 * eight coins in general circulation:
 * 
 * 1p, 2p, 5p, 10p, 20p, 50p, �1 (100p) and �2 (200p). It is possible to make �2
 * in the following way:
 * 
 * 1�100p + 1�50p + 2�20p + 1�5p + 1�2p + 3�1p How many different ways can �2 be
 * made using any number of coins?
 */

package com.euler.initalproblem;

public class Problem31 {

	static int[] coin = { 1, 2, 5, 10, 20, 50, 100, 200 };

	static int[] differentWays = new int[4];

	public static void main(String[] args) {
		// differentWays[0] = 0;
		// differentWays[1] = 1;
		// System.out.println(Arrays.toString(differentWays));
		int combinations = coinCombinations(200, 0);
		System.out.println(combinations);
	}

	public static int coinCombinations(int sum, int index) {
		if (sum == 0) {
			return 1;
		}
		if (index >= 8) {
			return 0;
		}
		if (sum < 0) {
			return 0;
		}
		int possible = coin[index];
		return coinCombinations(sum, index + 1) + coinCombinations(sum - possible, index);
	}

}
