/*
 * n! means n × (n − 1) × ... × 3 × 2 × 1
 * 
 * For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800, and the sum of the
 * digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.
 * 
 * Find the sum of the digits in the number 100!
 */
package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem20 {

	public static void main(String[] args) {
		String factorial = NumberUtil.getFactorialUsingMultiplication(9);
		System.out.println(factorial);
		System.out.println("Number of digits=" + factorial.length());
		int sum = 0;
		for (int i = 0; i < factorial.length(); i++) {
			int temp = factorial.charAt(i) - 48;
			sum += temp;
		}
		System.out.println(sum);
	}

}
