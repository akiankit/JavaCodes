package com.codechef.practice;

import java.util.Scanner;

public class NUMGAME2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int valueN = scanner.nextInt();
			if(valueN%4==1){
				System.out.println("ALICE");
			}else{
				System.out.println("BOB");
			}
		}
		scanner.close();
	}

}
