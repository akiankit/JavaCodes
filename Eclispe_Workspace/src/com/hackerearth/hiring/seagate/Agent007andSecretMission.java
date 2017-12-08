package com.hackerearth.hiring.seagate;

import java.util.Scanner;

public class Agent007andSecretMission {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int tests = sc.nextInt();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<tests;i++){
			String input = sc.next();
			int ones = countOnesInString(input);
			int n = sc.nextInt();
			long sum = getSumUptoN(n);
			if((sum&1) ==0){
				sb.append(ones).append("\n");
			}else{
				sb.append(input.length()-ones).append("\n");
			}
		}
		System.out.println(sb.toString());
		sc.close();
	}

	private static long getSumUptoN(int n) {
		if ((n & 1) == 0) {
			return ((n >> 1) * (n + 1));
		} else {
			return ((n + 1) >> 1) * n;
		}
	}

	private static int countOnesInString(String input) {
		int count = 0;
		for(int i=0;i<input.length();i++){
			if(input.charAt(i)=='1')
				count++;
		}
		return count;
	}
}
