package com.hiring.ezetap;

import java.util.Scanner;

public class Anagrams {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int tests = sc.nextInt();
		for(int i=0;i<tests;i++){
			String s1 = sc.next();
			String s2 = sc.next();
			if(isAnagarm(s1, s2)){
				System.out.println("YES");
			}else{
				System.out.println("NO");
			}
		}
		sc.close();
		
	}

	private static boolean isAnagarm(String s1, String s2) {
		int[] chars = new int[26];
		int[] digits = new int[10];
		if (s1.length() != s2.length())
			return false;
		for (int i = 0; i < s1.length(); i++) {
			char c = s1.charAt(i);
			if (c >= 'a' && c <= 'z') {
				chars[c - 'a']++;
			} else if (c >= '0' && c <= '9') {
				digits[c - '0']++;
			}
		}
		for (int i = 0; i < s2.length(); i++) {
			char c = s2.charAt(i);
			if (c >= 'a' && c <= 'z') {
				chars[c - 'a']--;
			} else if (c >= '0' && c <= '9') {
				digits[c - '0']--;
			}
		}
		for (int i = 0; i < 26; i++) {
			if (chars[i] != 0)
				return false;
		}
		for (int i = 0; i < 10; i++) {
			if (digits[i] != 0)
				return false;
		}
		return true;
	}
}
