package com.euler.initalproblem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Problem41 {

	public static void main(String[] args) {
		Map<Integer, Integer> map = new HashMap<>();
		int maxPerim = 1000;
		for (int a = 1; a < maxPerim / 2 + 1; a++) {
			for (int b = a; b < maxPerim / 2 + 1; b++) {
				for (int c = b; c < maxPerim / 2 + 1; c++) {
					if (!checkIfTraingleCanBeFormed(a, b, c)) {
						continue;
					}
					if (checkIfRightAngleTraingle(a, b, c)) {
						int perimiter = a + b + c;
						map.put(perimiter, map.getOrDefault(perimiter, 0) + 1);
					}
				}
			}
		}
		Set<Integer> keySet = map.keySet();
		int max = 0;
		int count = 0;
		for (Integer parim : keySet) {
			Integer value = map.get(parim);
			if (value > count) {
				count = value;
				max = parim;
			}
		}
		System.out.println(max);
		System.out.println(count);
		// System.out.println(map);
	}

	private static boolean checkIfRightAngleTraingle(int a, int b, int c) {
		int max = Math.max(a, Math.max(b, c));
		if (max == c) {
			return c * c == (b * b + a * a);
		} else if (max == b) {
			return b * b == (c * c + a * a);
		}
		return a * a == (c * c + b * b);
	}

	private static boolean checkIfTraingleCanBeFormed(int a, int b, int c) {
		return a + b > c && a + c > b && b + c > a;
	}

}
