package com.misc;

import java.util.Arrays;
import java.util.Scanner;

public class BeginEnd {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		int[] count = new int[266];
		for (int i = 0; i < input.length(); i++) {
			count[input.charAt(i)-'a'] = count[input.charAt(i)-'a'] + 1;
		}
		long res = 0L;
		for (int i = 0; i < 26; i++) {
			res = res + (count[i] * (count[i] + 1)) / 2;
		}
		System.out.println(res);
		scanner.close();
	}
}
