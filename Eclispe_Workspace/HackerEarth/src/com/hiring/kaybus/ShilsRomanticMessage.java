package com.hiring.kaybus;

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ShilsRomanticMessage {

	private static String getMessage(String input) {
		StringBuilder sb = new StringBuilder();
		char firstChar = input.charAt(0);
		if (firstChar == 'a')
			return input;
		char[] chars = new char[26];
		char value = 'a';
		for (int i = 0; i < 26; i++) {
			chars[firstChar - 'a'] = value;
			firstChar++;
			value++;
			if (firstChar > 'z') {
				firstChar = 'a';
			}
		}
		System.out.println(Arrays.toString(chars));
		for (int i = 0; i < input.length(); i++) {
			sb.append(chars[input.charAt(i) - 'a']);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String s = "Get Entertained";
		StringTokenizer st = new StringTokenizer(s, "t");
		while (st.hasMoreElements())
			System.out.print(st.nextToken());
		Scanner sc = new Scanner(System.in);
		int tests = sc.nextInt();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tests; i++) {
			String input = sc.next();
			sb.append(getMessage(input)).append("\n");
		}
		System.out.println(sb.toString());
		sc.close();
	}

}
