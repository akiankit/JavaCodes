package com.codechef.practice;

import java.util.Scanner;

public class BUYING2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int notesCount = scanner.nextInt();
			int sweetPrice = scanner.nextInt();
			int notesSum = 0;
			int minNote = 101;
			for (int j = 0; j < notesCount; j++) {
				int nextInt = scanner.nextInt();
				notesSum += nextInt;
				if(minNote > nextInt){
					minNote = nextInt;
				}
			}
			//System.out.println(minNote);
			int answer = (notesSum % sweetPrice > minNote) ? -1 : notesSum / sweetPrice;
			System.out.println(answer);
		}
		scanner.close();
	}

}
