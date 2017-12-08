package com.euler.initalproblem;

import java.util.LinkedList;
import java.util.List;

import com.initial.util.FileUtil;

public class Problem13 {

	private static String fileName =
	                "D:\\Android_workspace_SBrowser1_5\\ProjectEuler\\src\\projectEuler\\problem13Input";

	private static int numberLength = 100;

	public static void main(String[] args) throws Exception {
		String[] numbers = FileUtil.getDataFromFile(fileName);
		String sum = "";
		// System.out.println(sum);
		for (int i = 0; i < numberLength; i++) {
			sum = addTwoLargeNumbers(sum, numbers[i]);
		}
		System.out.println(sum.subSequence(0, 10));
	}

	static public String addLargeNumbersOfSameLengthUseBlock(String num1, String num2) {
		// StringBuilder sum = new StringBuilder();
		List<String> sum = new LinkedList<>();
		int blockSize = String.valueOf(Integer.MAX_VALUE).length() - 1;
		int lengthOfNumber = num1.length();
		if (lengthOfNumber <= blockSize) {
			String temp = String.valueOf(Integer.parseInt(num1) + Integer.parseInt(num2));
			if (temp.length() < num1.length()) {
				int diff = num1.length() - temp.length();
				temp = num1.substring(0, diff) + temp;
			}
			return temp;
		}
		// Assuming length of num1 and num2 are same.
		int carry = 0;
		for (int i = lengthOfNumber - 1; i >= 0; i -= blockSize) {
			int startIndex = i - blockSize + 1;
			if (startIndex < 0) {
				startIndex = 0;
			}
			String tempNum1 = num1.substring(startIndex, i + 1);
			String tempNum2 = num2.substring(startIndex, i + 1);
			String tempSum = String.valueOf(Integer.parseInt(tempNum1) + Integer.parseInt(tempNum2) + carry);
			if (tempSum.length() > blockSize && startIndex != 0) {
				carry = 1;
				tempSum = tempSum.substring(1);
			} else {
				carry = 0;
			}
			if (tempSum.length() < tempNum1.length()) {
				int diff = tempNum1.length() - tempSum.length();
				tempSum = tempNum1.substring(0, diff) + tempSum;
			}
			sum.add(0, tempSum);
		}
		StringBuilder finalSum = new StringBuilder();
		for (String string : sum) {
			finalSum.append(string);
		}
		/*
		 * if (!finalSum.toString().equals((new BigInteger(num1).add(new
		 * BigInteger(num2))).toString())) { System.out.println("temp"); }
		 */
		return finalSum.toString();
	}

	static public String addTwoLargeNumbers(String num1, String num2) {
		String res = "";
		if (num1.length() == num2.length()) {
			res = addLargeNumbersOfSameLengthUseBlock(num1, num2);
		} else {
			res = addLargeNumbersOfDifferentLength(num1, num2);
		}

		return res;
	}

	private static String addLargeNumbersOfDifferentLength(String num1, String num2) {
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
