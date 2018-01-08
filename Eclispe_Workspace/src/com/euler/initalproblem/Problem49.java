package com.euler.initalproblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.initial.util.NumberUtil;

public class Problem49 {

	public static void main(String[] args) {
		// long nanoTime = System.currentTimeMillis();
		List<Long> PrimesLessThan10000 = NumberUtil.getListOfPrimesLessThanN(10000);
		List<Long> PrimesLessThan1000 = NumberUtil.getListOfPrimesLessThanN(1000);
		PrimesLessThan10000.removeAll(PrimesLessThan1000);
		// System.out.println(System.currentTimeMillis() - nanoTime);

		// System.out.println(PrimesLessThan10000);
		// System.out.println(PrimesLessThan1000.size());
		Map<Long, List<Long>> primesCombinations = new HashMap<>();
		for (Long prime : PrimesLessThan10000) {
			String primeval = String.valueOf(prime);
			char[] charArray = primeval.toCharArray();
			Arrays.sort(charArray);
			String sortedNumVal = new String(charArray);
			long num = Long.valueOf(sortedNumVal);
			List<Long> combinations = primesCombinations.getOrDefault(num, new ArrayList<>(4));
			combinations.add(prime);
			primesCombinations.put(num, combinations);
		}
		// System.out.println(primesCombinations);
		// System.out.println(primesCombinations.size());
		Set<Long> keySet = primesCombinations.keySet();
		Map<Long, List<Long>> primesCombinations1 = new HashMap<>();
		for (Long prime : keySet) {
			if (primesCombinations.get(prime).size() >= 3) {
				primesCombinations1.put(prime, primesCombinations.get(prime));
			}
		}
		// System.out.println(primesCombinations1);
		// System.out.println(primesCombinations1.size());
		keySet = primesCombinations1.keySet();
		StringBuilder number = null;
		for (Long prime : keySet) {
			if (prime == 1478) {
				continue;
			}
			List<Long> list = primesCombinations1.get(prime);
			for (int i = 0; i < list.size() - 2; i++) {
				long long1 = list.get(i);
				long long2 = list.get(i + 1);
				long long3 = list.get(i + 2);
				if (long3 - long2 == long2 - long1) {
					// System.out.println("diff=" + (long3 - long2));
					number = new StringBuilder();
					number = number.append(long1).append(long2).append(long3);
					break;
				}
			}
			if (number != null) {
				break;
			}

		}
		System.out.println(number);
		// System.out.println(System.currentTimeMillis() - nanoTime);
	}

}
