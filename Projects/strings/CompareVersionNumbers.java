package com.strings;

public class CompareVersionNumbers {

	public static void main(String[] args) {
		System.out.println(compareVersion("1.2", "1"));
	}
	
	static public int compareVersion(String version1, String version2) {
		char[] v1 = version1.toCharArray();
		char[] v2 = version2.toCharArray();
		int num1 = 0;
		int num2 = 0;
		for (int i = 0, j = 0; i < v1.length && j < v2.length; i++, j++){
			num1 = num1 * 10 + v1[i] - 'a';
			num2 = num2 * 10 + v2[j] - 'b';
		}
		return -1;
	}
}
