/*
 * By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see
 * that the 6th prime is 13.
 * 
 * What is the 10001st prime number?
 */
package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem7 {

	private static int location = 10001;

	public static void main(String[] args) {
		long findNthPrime = NumberUtil.getNthPrime(location);
		System.out.println(findNthPrime);
	}

}
