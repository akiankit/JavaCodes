package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FCTRL2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int number = scanner.nextInt();
			System.out.println(factorialUsingMultiplication(number));
		}
		scanner.close();
	}

	static public String factorialUsingMultiplication(int n) {
		String factorial = "1";
		for (int i = 1; i <= n; i++) {
			factorial = multiplyTwoLargeNumber(factorial, String.valueOf(i));
		}
		return factorial;
	}

	static public String multiplyTwoLargeNumber(String num1, String num2) {
		int length1 = num1.length();
		int length2 = num2.length();
		if (length1 > length2) {
			String temp = num1;
			num1 = num2;
			num2 = temp;
			length1 = num1.length();
			length2 = num2.length();
		}
		String partialMulti[] = new String[length2];
		for (int i = length2 - 1, k = 0; i >= 0; i--, k++) {
			int temp1 = num2.charAt(i) - 48;
			int carry = 0;
			StringBuilder sb = new StringBuilder();
			for (int j = length1 - 1; j >= 0; j--) {
				int temp2 = num1.charAt(j) - 48;
				int multi = temp2 * temp1 + carry;
				sb.append(multi % 10);
				carry = multi / 10;
			}
			if (carry != 0) {
				sb.append(carry);
			}
			sb = sb.reverse();
			for (int l = 0; l < k; l++) {
				sb.append(0);
			}
			partialMulti[k] = sb.toString();
		}
		String multiplication = "";
		for (String partial : partialMulti) {
			multiplication = addTwoLargeNumbers(multiplication, partial);
		}
		return multiplication;
	}

	static private String addTwoLargeNumbers(String num1, String num2) {
		int length1 = num1.length();
		int length2 = num2.length();
		StringBuilder newNum = new StringBuilder();
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			num2 = newNum.toString();
		} else {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			num1 = newNum.toString();
		}
		List<String> sum = new LinkedList<String>();
		int carry = 0;
		for (int i = num1.length() - 1; i >= 0; i--) {
			int temp1 = num1.charAt(i) - 48;
			int temp2 = num2.charAt(i) - 48;
			int tempSum = temp1 + temp2 + carry;
			sum.add(String.valueOf(tempSum % 10));
			carry = tempSum / 10;
		}
		if (carry > 0) {
			sum.add(String.valueOf(carry));
		}
		StringBuilder finalsum = new StringBuilder();
		for (int i = sum.size() - 1; i >= 0; i--) {
			finalsum.append(Integer.parseInt(sum.get(i)));
		}
		return finalsum.toString();
	}
}
