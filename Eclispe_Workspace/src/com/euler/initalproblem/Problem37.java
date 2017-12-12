package com.euler.initalproblem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.initial.util.NumberUtil;

public class Problem37 {

	private static int[] first = { 2, 3, 5, 7 };
	private static int[] last = { 3, 7 };
	private static int[] mid = { 1, 3, 7, 9 };

	private static Map<Long, Boolean> primes1 = new HashMap<>(10000);

	private static List<Long> primes = new ArrayList<>(11);

	public static void main(String[] args) {
		primes.add(23l);
		primes.add(37l);
		primes.add(53l);
		primes.add(73l);
		for (long l = 101; primes.size() < 11; l += 2) {
			// System.out.println(l);
			String value = String.valueOf(l);
			if (!test(value)) {
				continue;
			}
			checkIfAllArePrimes(l);
		}
		System.out.println(primes);
		long sum = 0;
		for (int i = 0; i < 11; i++) {
			sum = sum + primes.get(i);
		}
		System.out.println(sum);
		// checkIfAllArePrimes(129);
	}

	private static boolean isPrime(long l) {
		if (primes1.containsKey(l)) {
			return primes1.get(l);
		}
		primes1.put(l, NumberUtil.isPrime(l));
		return primes1.get(l);
	}

	private static void checkIfAllArePrimes(long l) {
		// if (!NumberUtil.isPrime(l)) {
		// return;
		// }
		long l1 = l;
		int numDigits = 0;
		while (l1 > 0) {
			if (!isPrime(l1)) {
				return;
			}
			numDigits++;
			l1 = l1 / 10;

		}
		long factor = (long) Math.pow(10, numDigits - 1);
		long l2 = l;
		while (factor > 0) {
			if (factor != 1) {
				l2 = l2 % factor;
				if (!isPrime(l2)) {
					return;
				}
			}

			factor = factor / 10;
		}
		primes.add(l);
		System.out.println(primes);
	}

	private static boolean test(String num) {
		int firstChar = num.charAt(0) - '0';
		if (firstChar != 2 && firstChar != 3 && firstChar != 5 && firstChar != 7) {
			return false;
		}
		int lastChar = num.charAt(num.length() - 1) - '0';
		if (lastChar != 3 && lastChar != 7) {
			return false;
		}
		for (int i = 1; i < num.length() - 2; i++) {
			int midChar = num.charAt(i) - '0';
			if (midChar != 1 && midChar != 3 && midChar != 7 && midChar != 9) {
				return false;
			}
		}
		return true;
	}

}
