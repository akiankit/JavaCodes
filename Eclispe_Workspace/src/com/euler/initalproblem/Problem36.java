package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem36 {

	public static void main(String[] args) {
		int limit = 1000000;
		long n = 0l;
		int sum = 0;
		while (n < limit) {
			n = 0;
			// System.out.println(n);
			if (n % 2 != 0) {
				String base2String = Long.toBinaryString(n);
				if (NumberUtil.isPalindrome(base2String)) {
					System.out.println(n + " " + base2String);
					sum += n;
				}
			}
		}
		System.out.println(sum);
	}

}
