/*
 * A palindromic number reads the same both ways. The largest palindrome made
 * from the product of two 2-digit numbers is 9009 = 91 � 99.
 * 
 * Find the largest palindrome made from the product of two 3-digit numbers.
 */
package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem4 {

	private static int numDigits = 3;

	private static int ten = 10;

	public static void main(String[] args) {
		int lowLimit = (int) Math.pow(ten, numDigits + 1);
		int maxNumberInGivendigits = (int) (Math.pow(ten, numDigits) - 1);
		int minNumberInGivenDigits = (int) (Math.pow(ten, numDigits - 1) + 1);
		int highLimit = maxNumberInGivendigits * maxNumberInGivendigits;
		boolean found = false;
		System.out.println("lowLimit =" + lowLimit + " highLimit =" + highLimit);
		int i = highLimit;
		for (; i >= lowLimit; i--) {
			if (true == NumberUtil.isPalindrome(i) && false == NumberUtil.isPrime(i)) {
				for (int j = maxNumberInGivendigits; j >= minNumberInGivenDigits; j--) {
					if (i % j == 0) {
						int num2 = i / j;
						if (NumberUtil.getNumOfDigits((long) j) == numDigits
						                && NumberUtil.getNumOfDigits((long) num2) == numDigits) {
							System.out.println("Product of " + j + " and " + i / j);
							found = true;
							break;
						}
					}
				}
			}
			if (true == found) {
				break;
			}
		}
		System.out.println(i);
	}

}
