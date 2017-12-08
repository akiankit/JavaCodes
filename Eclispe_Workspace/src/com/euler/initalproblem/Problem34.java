/*
 * 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
 * 
 * Find the sum of all numbers which are equal to the sum of the factorial of
 * their digits.
 * 
 * Note: as 1! = 1 and 2! = 2 are not sums they are not included.
 * 
 * 
 */
package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem34 {

	// Deciding the upper bound.
	// If n is a natural number of d digits that is a factorion, then 10^d − 1 ≤
	// n ≤ 9!d and this fails to hold for d ≥ 8.
	// Thus n has 7 digits and the maximum sum of factorials of digits for a 7
	// digit number is 9!7 = 2,540,160, which is the upper bound.
	public static void main(String[] args) {
		int sum = 0;
		for (int i = 10; i < 2540160; i++) {
			if (i == sumOfFactorials(i)) {
				sum = sum + i;
			}
		}
		System.out.println(sum);
	}

	static private int sumOfFactorials(int num) {
		int[] factorials = new int[10];
		int sum = 0;
		while (num > 0) {
			int digit = num % 10;
			num = num / 10;
			int fact = factorials[digit];
			if (fact == 0) {
				fact = Integer.parseInt(NumberUtil.getFactorialUsingMultiplication(digit));
				factorials[digit] = fact;
			}
			sum = sum + fact;
		}
		return sum;
	}
}
