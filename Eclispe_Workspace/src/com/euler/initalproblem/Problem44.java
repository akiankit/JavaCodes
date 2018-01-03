package com.euler.initalproblem;

import java.util.HashSet;
import java.util.Set;

public class Problem44 {

	public static void main(String[] args) throws Exception {
		Set<Long> numbers = new HashSet<>();
		int count = 10000;
		long[] numArrays = new long[count];
		for (long l = 1; l < count; l++) {
			long num = (l * (3 * l - 1)) / 2;
			numArrays[(int) (l - 1)] = num;
			// System.out.println(l + "=>" + num);
			numbers.add(num);
		}
		long minDiff = Integer.MAX_VALUE;
		long num1 = 0;
		long num2 = 0;
		for (int i = 0; i < count; i++) {
			for (int j = i; j < count; j++) {
				long diff = numArrays[j] - numArrays[i];
				long sum = numArrays[j] + numArrays[i];
				if (numbers.contains(diff) && numbers.contains(sum)) {
					minDiff = Math.min(minDiff, diff);
					if (minDiff == diff) {
						num1 = j;
						num2 = i;

					}
				}
			}
		}
		System.out.println(num1 + "," + num2);
		System.out.println(minDiff);
	}

}
