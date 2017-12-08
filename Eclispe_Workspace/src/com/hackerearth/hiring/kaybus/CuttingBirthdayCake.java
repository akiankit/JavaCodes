package com.hackerearth.hiring.kaybus;

import java.util.Scanner;

public class CuttingBirthdayCake {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		StringBuilder sb = new StringBuilder();
		int tests = sc.nextInt();
		for (int i = 0; i < tests; i++) {
			double R = sc.nextInt();
			double A = sc.nextInt();
			double B = sc.nextInt();
			double PIE = 3.14;
			double circleArea = PIE * R * R;
			double cutterArea = A * B;

			if (circleArea < cutterArea) {
				sb.append("EQUAL").append("\n");
			} else {
				sb.append("ALICE").append("\n");
			}

		}
		System.out.println(sb.toString());
		sc.close();
	}
}
