package com.hiring.ezetap;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TheRevesedNumbers {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int tests = sc.nextInt();
		for (int i = 0; i < tests; i++) {
			String s1 = sc.next();
			String s2 = sc.next();
			String _s1 = removeLeadingZeroes(new StringBuilder(s1).reverse());
			String _s2 = removeLeadingZeroes(new StringBuilder(s2).reverse());
			String res = addLargeNumbersOfDifferentLength(_s1, _s2);
			res = removeLeadingZeroes(new StringBuilder(res).reverse());
			System.out.println(res);
		}
		sc.close();
	}

	static public String addLargeNumbersOfSameLengthUseBlock(String num1,
			String num2) {
		// StringBuilder sum = new StringBuilder();
		List<Integer> sum = new LinkedList<Integer>();
		int blockSize = String.valueOf(Integer.MAX_VALUE).length() - 1;
		int lengthOfNumber = num1.length();
		if (lengthOfNumber <= blockSize)
			return String.valueOf(Integer.parseInt(num1)
					+ Integer.parseInt(num2));
		// Assuming length of num1 and num2 are same.
		int carry = 0;
		for (int i = lengthOfNumber - 1; i >= 0; i -= blockSize) {
			int startIndex = i - blockSize + 1;
			if (startIndex < 0) {
				startIndex = 0;
			}
			String tempNum1 = num1.substring(startIndex, i + 1);
			String tempNum2 = num2.substring(startIndex, i + 1);
			String tempSum = String.valueOf(Integer.parseInt(tempNum1)
					+ Integer.parseInt(tempNum2) + carry);
			if (tempSum.length() > blockSize && startIndex != 0) {
				carry = 1;
				tempSum = tempSum.substring(1);
			} else {
				carry = 0;
			}
			sum.add(0, Integer.parseInt(tempSum));
		}
		StringBuilder finalSum = new StringBuilder();
		for (Integer integer : sum) {
			finalSum.append(integer);
		}
		return finalSum.toString();
	}

	private static String removeLeadingZeroes(StringBuilder result) {
		int i = 0;
		while (i < result.length() && result.charAt(i) == '0')
			i++;
		return result.substring(i);
	}

	private static String addLargeNumbersOfDifferentLength(String num1,
			String num2) {
		int length1 = num1.length();
		int length2 = num2.length();
		StringBuilder newNum = new StringBuilder();
		String sum = "";
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			sum = addLargeNumbersOfSameLengthUseBlock(num1, newNum.toString());
		} else {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			sum = addLargeNumbersOfSameLengthUseBlock(newNum.toString(), num2);
		}
		return sum;
	}

}
