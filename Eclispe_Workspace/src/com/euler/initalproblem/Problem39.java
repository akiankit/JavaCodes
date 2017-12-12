package com.euler.initalproblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.initial.util.NumberUtil;

public class Problem39 {

	private static Map<Long, List<Long>> arraysMap = new HashMap<>(100);

	public static void main(String[] args) {
		arraysMap.clear();
		List<Long> combinations = getCombinations(1234567, 7);
		System.out.println(combinations.size());
		long max = 0;
		Collections.sort(combinations, Collections.reverseOrder());
		System.out.println(combinations.get(0));
		for (Long combination : combinations) {
			if (NumberUtil.isPrime(combination)) {
				max = combination;
				break;
			}
		}
		System.out.println(max);
		List<Integer> list = new ArrayList<>();
		int last = 123;
		for (int i = 4; i < 10; i++) {
			int e = last * 10 + i;
			list.add(e);
			last = e;
		}
		// System.out.println(list);
	}

	public static List<Long> getCombinations(long n, int len) {
		if (arraysMap.get(n) != null) {
			return arraysMap.get(n);
		}
		List<Long> list = new ArrayList<>();
		if (len == 1) {
			if ((n & 1) != 0 && n != 5)
				list.add(n);
			return list;
		}
		int digits = NumberUtil.getNumOfDigits(n);
		long pow = (long) Math.pow(10, digits - 1);
		long[] rotations = NumberUtil.getAllCircularRotations(n * 1l);
		// System.out.println(Arrays.toString(rotations));
		for (long number : rotations) {
			long firstDigit = number / pow;
			// if (firstDigit != 9) {
			// continue;
			// }
			List<Long> combinations = getCombinations((number % pow), len - 1);
			for (Long combination : combinations) {
				// if ((combination & 1) == 0) {
				// continue;
				// }
				list.add(firstDigit * pow + combination);
			}
		}
		if (n == 2) {
			arraysMap.put(n, list);
		}
		return list;
	}

}
