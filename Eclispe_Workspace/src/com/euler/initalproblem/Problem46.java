package com.euler.initalproblem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.initial.util.NumberUtil;

public class Problem46 {

	static Set<Long> primes = null;
	static Map<Long, Boolean> primeMap = new HashMap<>();
	static {
		// List<Long> listOfPrimesLessThanN =
		// NumberUtil.getListOfPrimesLessThanN(1000000);
		// primes = new HashSet<>(listOfPrimesLessThanN);
	}

	public static void main(String[] args) throws Exception {
		long start = 101;
		long end = 10001;
		for (long i = start; i <= end; i += 2) {
			if (isPrime(i)) {
				// System.out.println(i + "=>prime");
				continue;
			}
			boolean conjectureTrue = isConjectureTrue(i);
			// System.out.println(i + "=>" + conjectureTrue);
			if (!conjectureTrue) {
				System.out.println(i);
				break;
			}
		}
	}

	private static boolean isConjectureTrue(long num) {
		long maxNum = (num - 2) >> 1;
		maxNum = (long) Math.sqrt(maxNum);
		for (long l = 1; l <= maxNum; l++) {
			long diff = num - 2 * l * l;
			if (isPrime(diff)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isPrime(long diff) {
		if (primeMap.containsKey(diff)) {
			return primeMap.get(diff);
		}
		primeMap.put(diff, NumberUtil.isPrime(diff));
		return primeMap.get(diff);
	}

}
