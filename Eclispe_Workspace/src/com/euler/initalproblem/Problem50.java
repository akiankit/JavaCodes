package com.euler.initalproblem;

import java.util.Set;
import java.util.TreeSet;

import com.initial.util.NumberUtil;

public class Problem50 {

	public static void main(String[] args) {
		// int n = 1000000;
		int n = 1000000;
		Set<Long> primesList = NumberUtil.getPrimesListLessThanNSieve(n);
		TreeSet<Long> sortedPrimes = new TreeSet<>(primesList);
		long[] sums = new long[sortedPrimes.size()];
		int i = -1;
		long maxSum = -1;
		int consecutiveCount = -1;
		for (Long prime : sortedPrimes) {
			i++;
			if (i == 0) {
				sums[i] = prime;
				continue;
			}
			sums[i] = sums[i - 1] + prime;
			if (primesList.contains(sums[i])) {
				maxSum = sums[i];
				consecutiveCount = i + 1;
			}
		}
		System.out.println("maxSum=" + maxSum + "consecutiveCount=" + consecutiveCount);
		// System.out.println(sortedPrimes);
		// System.out.println(sums.length);
		for (i = 0; i < sums.length; i++) {
			int consecutiveCount1 = consecutiveCount;
			for (int j = sums.length - 1; j > i + consecutiveCount1; j--) {
				long diff = sums[j] - sums[i];
				if (diff > n || !sortedPrimes.contains(diff)) {
					continue;
				}
				System.out.println("num1:" + sums[i] + "\tnum2:" + sums[j] + "\tdiff:" + diff);
				consecutiveCount = j - i;
				maxSum = diff;
			}
		}
		System.out.println("maxSum=" + maxSum + "consecutiveCount=" + consecutiveCount);
	}

}
