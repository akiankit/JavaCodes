package com.euler.initalproblem;

import com.initial.util.NumberUtil;

public class Problem40 {

	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		junk();
		System.out.println(System.currentTimeMillis() - time1);
		time1 = System.currentTimeMillis();
		int nextIndex = 1;
		int max = 1000000;
		int index = 1;
		StringBuilder sb = new StringBuilder();
		int num = 1;
		while (index <= max) {
			int numOfDigits = NumberUtil.getNumOfDigits(num);
			int newIndex = index + numOfDigits;
			if (newIndex > nextIndex) {
				long[] digits = NumberUtil.getDigitsInNumber(num);
				int offset = nextIndex - index;
				sb.append(digits[offset]);
				nextIndex = nextIndex * 10;
			}
			index = newIndex;
			num++;
		}
		System.out.println(System.currentTimeMillis() - time1);
		System.out.println(sb);
	}

	static void junk() {
		StringBuilder sb = new StringBuilder("0");
		int num = 1;
		int max = 1000000;
		while (sb.length() <= max) {
			sb.append(num++);
		}
		System.out.println(num);
		StringBuilder sb2 = new StringBuilder().append(sb.charAt(1)).append(sb.charAt(10)).append(sb.charAt(100))
		                .append(sb.charAt(1000)).append(sb.charAt(10000)).append(sb.charAt(100000))
		                .append(sb.charAt(max));
		System.out.println(sb2);
	}
}
