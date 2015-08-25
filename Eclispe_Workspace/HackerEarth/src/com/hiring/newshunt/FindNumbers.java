package com.hiring.newshunt;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FindNumbers {

	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = br.readLine();
		long[] nums = null;
		nums = findAllNumbers(line);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nums.length; i++) {
			sb.append(nums[i] + "\n");
		}
		System.out.println(sb.toString());
	}
	
	public static long[] findAllNumbers(String line) {
		long[] nums = new long[line.length()];
		int j = 0;
		for (int i = 0; i < line.length();) {
			if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
				long num = line.charAt(i) - '0';
				i++;
				while (i < line.length() && line.charAt(i) >= '0'
						&& line.charAt(i) <= '9') {
					num = num * 10 + (line.charAt(i) - '0');
					i++;
				}
				nums[j++] = num;
			} else {
				i++;
			}
		}
		long[] finalNums = new long[j];
		for(int i=0;i<j;i++) {
			finalNums[i] = nums[i];
		}
		return finalNums;
	}
}
