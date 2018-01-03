package com.euler.initalproblem;

public class Problem45 {

	public static void main(String[] args) throws Exception {
		for (long i = 287;; i += 2) {
			long num = (i * (i + 1)) >> 1;
			if (isPentagonal(num)) {
				System.out.println(num);
				break;
			}
		}
		// for (int i = 1; i < 1000; i++) {
		// if (isPentagonal(i)) {
		// System.out.println(i);
		// }
		// }
	}

	private static boolean isPentagonal(long num) {
		// num = (num << 4 + num << 3) + 1;
		// System.out.print(num);
		num = num * 24 + 1;
		double num1 = (Math.sqrt(num) + 1) / 6.0;
		// System.out.println("," + num1);
		return num1 == (int) num1;
	}

}
