package com.euler.initalproblem;

import java.util.Set;
import java.util.TreeSet;

import com.initial.util.NumberUtil;

public class Problem51 {

	public static void main(String[] args) {
		int n = 1000000;
		int m = 10000000;
		long time1 = System.currentTimeMillis();
		Set<Long> primesInRange = NumberUtil.getPrimesInRange(n, m);
		System.out.println(System.currentTimeMillis() - time1);
		time1 = System.currentTimeMillis();
		Set<Long> primes1 = NumberUtil.getPrimesListLessThanNSieve(m);
		primes1.removeAll(NumberUtil.getPrimesListLessThanNSieve(n));
		System.out.println(System.currentTimeMillis() - time1);
		time1 = System.currentTimeMillis();
		System.out.println(new TreeSet<>(primes1));
		System.out.println(new TreeSet<>(primesInRange));
	}

}
