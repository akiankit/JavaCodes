package com.hackerrank.misc.problems;

import java.util.Scanner;

public class MinimumTransformationCost {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();
		for(int i=0;i<t;i++){
			String word1 = sc.next();
			String word2 = sc.next();
			int del = sc.nextInt();
			int ins = sc.nextInt();
			int rep = sc.nextInt();
			if(isAnagarm(word1, word2)){
				System.out.println("0");
			}else
				System.out.println(minDistance(word1, word2, ins, del, rep));
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

	public static int minDistance(String word1, String word2, int ins, int del,
			int rep) {
		int[] arr1 = new int[26], arr2 = new int[26];
		rep = ins + del > rep ? rep : ins + del;
		for (int i = 0; i < word1.length(); i++) {
			arr1[word1.charAt(i) - 'a']++;
		}
		for (int i = 0; i < word2.length(); i++) {
			arr2[word2.charAt(i) - 'a']++;
		}
		int sum1 = 0;
		int sum2 = 0;
		for (int i = 0; i < 26; i++) {
			int min = Math.min(arr1[i], arr2[i]);
			arr1[i] = arr1[i] - min;
			arr2[i] = arr2[i] - min;
			sum1 = sum1 + arr1[i];
			sum2 = sum2 + arr2[i];
		}
		int cost = 0;
		if (sum1 > sum2) {
			int diff = sum1 - sum2;
			cost = diff * del;
			cost = cost + sum2 * rep;
		} else if (sum2 > sum1) {
			int diff = sum2 - sum1;
			cost = diff * ins;
			cost = cost + sum1 * rep;
		} else if (sum2 == sum1) {
			cost = sum1 * rep;
		}
		return cost;
	}
}
