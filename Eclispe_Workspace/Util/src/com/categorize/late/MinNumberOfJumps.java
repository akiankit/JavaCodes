package com.categorize.late;

//http://www.geeksforgeeks.org/minimum-number-of-jumps-to-reach-end-of-a-given-array/

public class MinNumberOfJumps {

	private static int[] input = { 1, 3, 5, 8, 9, 2, 7, 6, 8, 9 };

	public static void main(String[] args) {
		System.out.println(minNumberOfJumps(input, 1, new int[input.length]));
	}

	private static int minNumberOfJumps(int[] input2, int startIndex, int[] steps) {
		int num = input[startIndex];
		if (startIndex == input.length) {
			return 0;
		}
		if (num != 0) {
			int stepsCount = input.length - startIndex - 1;
			for (int j = startIndex + 1; j <= num + startIndex && j < input.length; j++) {
				int tempCount = 0;
				if (steps[j] != 0) {
					tempCount = steps[j];
				} else {
					tempCount = minNumberOfJumps(input2, j, steps);
				}
				if (tempCount + 1 < stepsCount) {
					stepsCount = tempCount + 1;
				}
			}
			steps[startIndex] = stepsCount;
		}
		return steps[startIndex];
	}

}
