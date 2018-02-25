package com.hackerrank.misc.problems;

import java.util.Scanner;

public class SherlockAndCost {

	static int[][] sums;

	static int cost(int[] arr) {
		int len = arr.length;
		int[] sum = new int[2];
		sum = getMaxCost(arr, len - 1);
		// sums = new int[len][2];
		// for (int i=1; i<len; i++) {
		// sums[i] = getMaxCost(arr, i);

		// }
		// int[] sum = sums[len-1];
		return Math.max(sum[0], sum[1]);
	}

	/**
	 * Because we have to get max sum possible, we should maximize the
	 * difference of adjacent elements(as per sum formula described in problem).
	 * Now because elements can range from {1 to Bi} so considering
	 * max-difference case either it will be 1 or Bi.</br>
	 * Lets take array(say sum-array) of 2 elements and store max sum of
	 * elements till this index in this array such that.
	 * <li>index 0 will have max sum such that last element of array i.e. Ai is
	 * 1</li>
	 * <li>index 1 will have max sum such that last element of array i.e. Ai is
	 * max value</li> </br>
	 * Keep popuplating value for each index maintaining above constraint and
	 * then finally get max of both elements in sum-array.
	 *
	 * @param arr
	 * @param i
	 * @return sum array
	 */
	static int[] getMaxCost(int[] arr, int i) {
		int[] sum = new int[2];
		if (i < 1) {
			return sum;
		}
		if (i == 1) {
			sum[0] = Math.abs(arr[0] - 1);
			sum[1] = Math.abs(arr[1] - 1);
			// System.out.println("i=" + (i + 1) + ",sum=" +
			// Arrays.toString(sum));
			return sum;
		}
		sum = getMaxCost(arr, i - 1);
		// sum = sums[i-1];
		// System.out.println(Arrays.toString(sum));
		int num = arr[i];
		int prevNum = arr[i - 1];
		int sum0 = sum[0];
		sum[0] = sum[1] + Math.abs(prevNum - 1);
		sum[1] = Math.max(sum0 + Math.abs(num - 1), sum[1] + Math.abs(prevNum - num));
		// System.out.println("i=" + (i + 1) + ",sum=" + Arrays.toString(sum));
		return sum;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int t = in.nextInt();
		for (int a0 = 0; a0 < t; a0++) {
			int n = in.nextInt();
			int[] arr = new int[n];
			for (int arr_i = 0; arr_i < n; arr_i++) {
				arr[arr_i] = in.nextInt();
			}
			int result = cost(arr);
			System.out.println(result);
		}
		in.close();
	}
}
