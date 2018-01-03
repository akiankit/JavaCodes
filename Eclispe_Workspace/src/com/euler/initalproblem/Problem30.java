/*
 * Surprisingly there are only three numbers that can be written as the sum of
 * fourth powers of their digits:
 * 
 * 1634 = 1^4 + 6^4 + 3^4 + 4^4 8208 = 8^4 + 2^4 + 0^4 + 8^4 9474 = 9^4 + 4^4 +
 * 7^4 + 4^4 As 1 = 1^4 is not a sum it is not included.
 * 
 * The sum of these numbers is 1634 + 8208 + 9474 = 19316.
 * 
 * Find the sum of all the numbers that can be written as the sum of fifth
 * powers of their digits.
 */
package com.euler.initalproblem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Problem30 {

	static Map<Integer, Integer> power = null;

	/**
	 * 9^5 is 59949. So if for 5 digits if all digits are 9 which will lead to
	 * max sum(599949*5=299745). That will become 6 digit.<br>
	 * For 6 digit if all digits are 9 then num will be (59949*6=359694), which
	 * is also of 6 digits. <br>
	 * So maximum number to check is of numbers with 6 digit. I am not sure but
	 * I think we should check up to only 359694 number.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		long max = 1000000;
		power = new HashMap<>(10);
		for (int i = 0; i <= 9; i++) {
			power.put(i, (int) Math.pow(i, 5));
		}
		List<Integer> nums = new LinkedList<>();
		for (int i = 11; i < max; i++) {
			if (isTrue(i)) {
				nums.add(i);
			}
		}
		long sum = 0;
		for (Integer num : nums) {
			sum += num;
		}
		System.out.println(nums);
		System.out.println(sum);
		System.out.println(power);
	}

	private static boolean isTrue(int num) {
		int num1 = num;
		int sum = 0;
		while (num > 0) {
			sum += power.get((num % 10));
			num = num / 10;
		}
		return sum == num1;
	}

	long findMaxLimit(int power) {
		long limit = 0l;

		return limit;
	}

}
