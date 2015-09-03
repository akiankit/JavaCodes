package com.codechef.practice;

import java.util.Scanner;

public class CHEFLUCK {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			boolean solutionFound = false;
			int valueN = scanner.nextInt();
			int count4 = valueN;
			for (; count4 >= 0; count4 -= 4) {
				int count7 = valueN - count4;
				if (count7 == 0) {
					if (count4 % 7 == 0) {
						solutionFound = true;
						break;
					}
				} else if (count4 == 0) {
					if (count7 % 4 == 0) {
						solutionFound = true;
						break;
					}
				} else{
					if ((count4 % 7 == 0) && (count7 % 4 == 0)) {
						solutionFound = true;
						break;
					}
				}
				
			}
			if (solutionFound == false) {
				System.out.println("-1");
			} else {
				System.out.println(count4);
			}
		}
		scanner.close();
	}

}
