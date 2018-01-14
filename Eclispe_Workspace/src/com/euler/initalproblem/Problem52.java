package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem52 {

	public static void main(String[] args) {
		solve();
	}

	/**
	 * If we check for each digits then we will find that we get atleast 5
	 * digits, So if we want to have same digits in all the multiplication from
	 * 1 to 6, at least it should have 5 digits, So we can start from numbers
	 * with 5 digits
	 */
	public static void solve() {
		for (long i = 100000; i < 1000010; i++) {
			int num = 2;
			long[] digits = NumberUtil.getDigitsCountsInNumber(i);
			while (num < 7) {
				long num1 = i * num;
				long[] digits1 = NumberUtil.getDigitsCountsInNumber(num1);
				num++;
				if (!areDigitsSame(digits, digits1)) {
					break;
				}
			}
			if (num == 7) {
				System.out.println(i);
				break;
			}
		}
	}

	public static boolean areDigitsSame(long[] digits, long[] digits1) {
		for (int i = 0; i < digits.length; i++) {
			if (digits[i] == 0 && digits1[i] != 0) {
				return false;
			}
			if (digits[i] != 0 && digits1[i] == 0) {
				return false;
			}
		}
		return true;
	}
}
