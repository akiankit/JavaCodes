package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem48 {

	public static void main(String[] args) {
		// System.out.println(Long.MAX_VALUE);
		// long calculateModPower = NumberUtil.calculateModPower(18, 18,
		// 10000000000l);
		// System.out.println(calculateModPower);
		// long nanoTime = System.nanoTime();
		long last10Digit = getLast10Digit();
		System.out.println(last10Digit);
		// System.out.println(System.nanoTime() - nanoTime);
	}

	public static long getLast10Digit() {
		long mod = (long) Math.pow(10, 10);
		// System.out.println(mod);
		long ans = 1;
		long end = 1000;
		long start = 2;
		for (long l = start; l <= end; l++) {
			long calculateModPower = NumberUtil.calculateModPower(l, l, mod);
			// System.out.println(calculateModPower);
			ans = (ans + calculateModPower) % mod;
		}

		if (ans < 0)
			ans += mod;
		return ans;
	}

}
