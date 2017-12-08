/*
 * The prime factors of 13195 are 5, 7, 13 and 29.
 * 
 * What is the largest prime factor of the number 600851475143 ?
 */
package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem3 {

	public static void main(String[] args) {

		int prime = 0;
		long num = 600851475143l;
		// long num = 13195;
		double sqrt = Math.sqrt(num);
		for (int i = 2; i <= sqrt; i++) {
			if (0 == num % i && true == NumberUtil.isPrime(i)) {
				prime = i;
			}
		}
		System.out.println(prime);
	}

}
