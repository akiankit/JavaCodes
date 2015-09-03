package com.categorize.late;

//http://www.youtube.com/watch?v=U-xOVWlcgmM
//http://www.geeksforgeeks.org/dynamic-programming-set-3-longest-increasing-subsequence/

public class LIS {

	private static int input[] = { 10, 22, 9, 33, 21, 50, 41, 60, 80 };
	private static int lis[] = new int[input.length];

	public static void main(String[] args) {
		getLISLength();
		for (int i = 0; i < lis.length; i++) {
			System.out.println(lis[i]);
		}
	}

	public static void getLISLength() {
		int n = input.length;
		int i = 0, j = 0;
		for (i = 0; i < n; i++) {
			lis[i] = 1;
		}
		
		
		for (i = 1; i < n; i++) {
			for (j = 0; j < i; j++) {
				if (input[i] > input[j] && lis[i] < lis[j] + 1) {
					lis[i] = lis[j] + 1;
				}
			}
		}
		int max = 0;
		for (i = 0; i < n; i++) {
			if (max < lis[i]) {
				max = lis[i];
			}
		}
	}

}
