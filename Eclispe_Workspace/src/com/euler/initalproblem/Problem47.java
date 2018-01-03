package com.euler.initalproblem;

import java.util.Set;

import com.initial.util.NumberUtil;

public class Problem47 {

	public static void main(String[] args) {
		Set<Long> primeFactors = NumberUtil.getPrimeFactors(253894);
		System.out.println(primeFactors);
		System.out.println(NumberUtil.getPrimeFactors(253895));
		System.out.println(NumberUtil.getPrimeFactors(253896));
		System.out.println(NumberUtil.getPrimeFactors(253897));
		System.out.println(getNumbers(4));
	}

	public static long getNumbers(int n) {
		long start = 210;
		for (long i = start;; i++) {
			Set<Long> primeFactors = NumberUtil.getPrimeFactors(i);
			int size = primeFactors.size();
			System.out.println(i + "=>" + size);
			if (size == n) {
				boolean possible = true;
				for (long j = i + 1; j <= i + n - 1; j++) {
					Set<Long> primeFactors1 = NumberUtil.getPrimeFactors(j);
					int size2 = primeFactors1.size();
					System.out.println(j + "=>" + size2);
					if (size2 != n) {
						possible = false;
						break;
					}
				}
				if (possible) {
					return i;
				}
			}
		}
	}
}
