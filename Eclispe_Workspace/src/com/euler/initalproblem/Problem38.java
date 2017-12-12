package com.euler.initalproblem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem38 {

	private static Map<Long, Boolean> primes1 = new HashMap<>(10000);

	private static List<Long> primes = new ArrayList<>(11);

	/**
	 * I think this number should defintely start with 1. Becuase first digit of
	 * pandigital number which we want is 9. And we want to first multiply by 1.
	 * So definitely 1st digit should be 1. Doing this check will optimize this
	 * code completely
	 */
	public static void main(String[] args) {
		long max = 0;
		long number1 = 0l;
		for (int i = 9; i < 9999; i++) {
			if (i % 10 == 0 || i % 10 == 5) {
				continue;
			}
			StringBuilder number = new StringBuilder();
			int k = 1;
			while (number.length() <= 9) {
				number.append(k * i);
				k++;
				if (number.length() == 9) {
					long pandigitalNumber = checkIfPandigitalNumber(number);
					if (max < pandigitalNumber) {
						max = pandigitalNumber;
						number1 = i;
					}
				}
			}
		}
		System.out.println(number1);
		System.out.println(max);
	}

	private static long checkIfPandigitalNumber(StringBuilder number) {
		boolean[] counts = new boolean[9];
		// System.out.println(number);
		for (int i = 0; i < 9; i++) {
			char char1 = number.charAt(i);
			if (char1 == '0') {
				return 0;
			}
			// System.out.println(char1);
			int index = char1 - '0' - 1;
			// System.out.println(index);
			counts[index] = true;
		}
		for (int i = 0; i < 9; i++) {
			if (!counts[i]) {
				return 0;
			}
		}
		return Long.valueOf(number.toString());
	}

}
