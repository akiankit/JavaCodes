/*
 * A perfect number is a number for which the sum of its proper divisors is
 * exactly equal to the number. For example, the sum of the proper divisors of
 * 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
 * 
 * A number n is called deficient if the sum of its proper divisors is less than
 * n and it is called abundant if this sum exceeds n.
 * 
 * As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the smallest
 * number that can be written as the sum of two abundant numbers is 24. By
 * mathematical analysis, it can be shown that all integers greater than 28123
 * can be written as the sum of two abundant numbers. However, this upper limit
 * cannot be reduced any further by analysis even though it is known that the
 * greatest number that cannot be expressed as the sum of two abundant numbers
 * is less than this limit.
 * 
 * Find the sum of all the positive integers which cannot be written as the sum
 * of two abundant numbers.
 */
package com.euler.initalproblem;

import java.util.List;

import com.initial.util.NumberUtil;

public class Problem23 {

	private static int limit = 28123;

	private static boolean[] isAbundant = new boolean[limit + 1];

	public static void main(String[] args) {
		long sum = 0l;
		for (int i = 1; i <= limit; i++) {
			isAbundant[i] = isAbundant(i);
		}
		for (int i = 1; i <= limit; i++) {
			if (isSumOfTwoAbundant(i) == false) {
				sum += i;
			}
		}
		System.out.println(sum);
	}

	private static boolean isSumOfTwoAbundant(int n) {
		for (int i = 1; i <= n / 2; i++) {
			if (isAbundant(i) && isAbundant(n - i)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAbundant(int i) {
		long sum = 0l;
		List<Long> properDivisor = NumberUtil.getProperDivisor(i);
		for (Long divisior : properDivisor) {
			sum += divisior;
		}
		if (sum > i) {
			return true;
		}
		return false;
	}

}
