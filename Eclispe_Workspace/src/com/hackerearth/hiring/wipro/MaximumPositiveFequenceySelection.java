package com.hackerearth.hiring.wipro;


public class MaximumPositiveFequenceySelection {

	public static void main(String[] args) {
		int[] arr = { 2, 15, 80, 30, 10, 8, 25 };// {2,30,15,10,8,25,80};
		System.out.println(maximumPositiveFrequency(arr, 7));
	}

	public static int maximumPositiveFrequency(int[] input1, int input2) {
		// System.out.println(Arrays.toString(input1));
		int[] left = new int[input2];
		int[] right = new int[input2];
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < input2; i++) {
			if (i == 0) {
				left[i] = 0;
				if (max < input1[i]) {
					max = input1[i];
				}
				if (min > input1[i]) {
					min = input1[i];
				}
				continue;
			}
			if (max < input1[i]) {
				max = input1[i];
				left[i] = max - min;
			} else if (min > input1[i]) {
				min = input1[i];
				left[i] = left[i - 1];
			} else {
				left[i] = left[i - 1];
			}

		}
		// System.out.println(Arrays.toString(left));
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		for (int i = input2 - 1; i >= 0; i--) {
			if (i == input2 - 1) {
				right[i] = 0;
				if (max < input1[i]) {
					max = input1[i];
				}
				if (min > input1[i]) {
					min = input1[i];
				}
				continue;
			}
			if (max < input1[i]) {
				max = input1[i];
				right[i] = right[i + 1];
			} else if (min > input1[i]) {
				min = input1[i];
				right[i] = max - min;
			} else {
				right[i] = right[i + 1];
			}
		}
		// System.out.println(Arrays.toString(right));
		max = 0;
		for (int i = 0; i < input2; i++) {
			if (left[i] + right[i] > max)
				max = left[i] + right[i];
		}
		return max;
	}
}