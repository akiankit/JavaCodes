package com.hiring.nationalinstruments;

import java.util.Scanner;

public class CrazyNumbers {

	private static long mod = (long) (Math.pow(10, 9) + 7);

	private static long[] vals = new long[1000000 + 1];

	static {
		vals[1] = 10;
		vals[2] = 17;
		long[] arr = { 1, 1, 2, 2, 2, 2, 2, 2, 2, 1 };
		// System.out.println(arr.length);
		for (int i = 3; i < vals.length; i++) {
			long[] temp = new long[10];
			temp[0] = arr[1];
			temp[9] = arr[8];
			for (int j = 1; j < 9; j++)
				temp[j] = (arr[j - 1] + arr[j + 1]) % mod;
			long sum = 0;
			for (int j = 0; j < 10; j++) {
				arr[j] = temp[j];
				sum = (sum + arr[j]) % mod;
			}
			vals[i] = sum;
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();
		for (int i = 0; i < t; i++) {
			int n = sc.nextInt();
			System.out.println(vals[n]);
		}
		sc.close();
	}

}
