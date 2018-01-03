package com.euler.initalproblem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.initial.util.NumberUtil;

public class Problem43 {

	private static Map<Long, List<Long>> arraysMap = new HashMap<>(100);

	private static long sum = 0;

	public static void main(String[] args) throws Exception {
		long time1 = System.currentTimeMillis();
		// List<Long> combinations = getCombinations(1406357289, 10);
		// System.out.println(Integer.MAX_VALUE);
		// System.out.println(combinations.size());
		// System.out.println(sum);
		// System.out.println("Time:" + (System.currentTimeMillis() - time1));
	}

	public static List<Long> getCombinations(long n, int len) {
		if (arraysMap.get(n) != null) {
			return arraysMap.get(n);
		}
		List<Long> list = new ArrayList<>();
		if (len == 1) {
			list.add(n);
			return list;
		}
		int digits = NumberUtil.getNumOfDigits(n);
		long pow = (long) Math.pow(10, digits - 1);
		long[] rotations = NumberUtil.getAllCircularRotations(n * 1l);
		// System.out.println(Arrays.toString(rotations));
		for (long number : rotations) {
			long firstDigit = number / pow;
			List<Long> combinations = getCombinations((number % pow), len - 1);
			for (Long combination : combinations) {
				long number1 = firstDigit * pow + combination;
				// if (len == 10 && !isDivisibleByAll(number1)) {
				// continue;
				// }
				// if (len == 10) {
				// sum += number1;
				// }
				list.add(number1);
			}
		}
		if (len == 2) {
			arraysMap.put(n, list);
		}
		return list;
	}

	private static boolean isDivisibleByAll(long number) {
		// System.out.println(number);
		// 1430952867 + 1460357289 + 1406357289 + 4130952867 + 4160357289
		// +4106357289
		if (number == 1406357289 || number == 1430952867) {
			System.out.println(number);
		}
		String value = String.valueOf(number);
		if (value.length() != 10) {
			// value = "0" + value;
			return false;
		}
		int num4 = value.charAt(3) - '0';
		int num6 = value.charAt(5) - '0';
		if ((num4 & 1) != 0) {
			return false;
		}
		if (num6 != 5 && num6 != 0) {
			return false;
		}
		int num5 = value.charAt(4) - '0';
		int num3 = value.charAt(2) - '0';
		if ((num3 + num4 + num5) % 3 != 0) {
			return false;
		}
		int num7 = value.charAt(6) - '0';
		if (((num5 * 10 + num6) - num7 * 2) % 7 != 0) {
			return false;
		}
		int num8 = value.charAt(7) - '0';
		if ((num6 - num7 + num8) % 11 != 0) {
			return false;
		}
		int num9 = value.charAt(8) - '0';
		if ((num7 * 100 + num8 * 10 + num9) % 13 != 0) {
			return false;
		}
		int num10 = value.charAt(9) - '0';
		if ((num8 * 100 + num9 * 10 + num10) % 17 != 0) {
			return false;
		}
		return true;
	}

}
