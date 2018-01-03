/*
 * Euler discovered the remarkable quadratic formula:
 * 
 * n² + n + 41
 * 
 * It turns out that the formula will produce 40 primes for the consecutive
 * values n = 0 to 39. However, when n = 40, 402 + 40 + 41 = 40(40 + 1) + 41 is
 * divisible by 41, and certainly when n = 41, 41² + 41 + 41 is clearly
 * divisible by 41.
 * 
 * The incredible formula n² − 79n + 1601 was discovered, which produces 80
 * primes for the consecutive values n = 0 to 79. The product of the
 * coefficients, −79 and 1601, is −126479.
 * 
 * Considering quadratics of the form:
 * 
 * n² + an + b, where |a| < 1000 and |b| < 1000
 * 
 * where |n| is the modulus/absolute value of n e.g. |11| = 11 and |−4| = 4 Find
 * the product of the coefficients, a and b, for the quadratic expression that
 * produces the maximum number of primes for consecutive values of n, starting
 * with n = 0.
 */
package com.euler.initalproblem;

import java.util.Set;
import java.util.TreeSet;

import com.initial.util.NumberUtil;

public class Problem27 {

	static Set<Long> primesToCheck = NumberUtil.getPrimesListLessThanNSieve(1000000);

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Set<Long> primes = NumberUtil.getPrimesListLessThanNSieve(1000);
		primes = new TreeSet<>(primes);
		System.out.println(primes);
		System.out.println(primes.size());
		int maxCount = -1;
		long product = -1;
		long a1 = -1;
		long b1 = -1;
		for (long prime : primes) {
			// System.out.println("=======================");
			long b = prime;
			// b = 1601;
			for (long a = -1000; a <= 1000; a++) {
				int count = 0;
				int n = 0;
				while (isPrime(n++, a, b)) {
					count++;
				}
				if (count > maxCount) {
					maxCount = count;
					product = a * b;
					a1 = a;
					b1 = b;
				}
			}
		}
		System.out.println("a1:" + a1 + ",b1:" + b1);
		System.out.println(maxCount);
		System.out.println(product);
		System.out.println(System.currentTimeMillis() - start);
	}

	private static boolean isPrime(int n, long a, long b) {
		long exp = n * n + a * n + b;
		// System.out.println("a=" + a + "\tb=" + b + "\tn=" + n + "\texp=" +
		// exp);
		return primesToCheck.contains(exp);
	}

}
