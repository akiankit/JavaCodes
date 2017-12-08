package com.hackerearth.challenges.week17;

import java.util.Scanner;

public class FindTheRobot {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			int N = sc.nextInt();
			sb.append(getPositionForN(N)).append("\n");
		}
		System.out.println(sb.toString());
		sc.close();
	}

	private static String getPositionForN(int N) {
		int start = (N / 4) * 2;
		int x = start * (-1);
		int y = start * (-1);
		int diff = N % 4;
		int temp = (N / 4) * 4; 
		if (diff == 1) {
			x = x + (temp + 1);
		} else if (diff == 2) {
			x = x + (temp + 1);
			y = y + (temp + 2);
		} else if (diff == 3) {
			x = x - 2;
			y = y * (-1) + 2;
		}
		return x + " " + y;
	}
}
