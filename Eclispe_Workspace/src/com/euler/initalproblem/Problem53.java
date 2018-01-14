package com.euler.initalproblem;

public class Problem53 {

	public static void main(String[] args) {
		solve();
	}

	public static void solve() {
		long max = 100;
		long start = 23;
		long limit = 1000000;
		long count = 0;
		for (long i = start; i <= max; i++) {
			System.out.println("==================i:" + i);
			long j = 1;
			long fact = 1;
			while (j <= i / 2 && fact <= limit) {
				fact = (fact * (i - j + 1)) / j;
				System.out.println("j:" + j + ",fact:" + fact);
				j++;
			}
			if (fact > limit) {
				long currNumCount = i + 1 - 2 * (j - 1);
				System.out.println("For :" + i + ",Count:" + currNumCount);
				count = count + currNumCount;
			}
		}
		System.out.println(count);
	}

	public static long getCount(long i, long l) {
		if ((i & 1) == 0) {
			return 2 * (i / 2 - l) + 1;
		}
		return 2 * (i / 2 - l);
	}

}
