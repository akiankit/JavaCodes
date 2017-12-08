package com.codechef.practice;

import java.util.Scanner;
public class LECANDY {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for(int i=0;i<testCases;i++){
			int elephantsCount = scanner.nextInt();
			int candiesCount = scanner.nextInt();
			long sum = 0;
			for(int j=0;j<elephantsCount;j++){
				sum += scanner.nextInt();
			}
			if(sum <=candiesCount){
				System.out.println("Yes");
			}else{
				System.out.println("No");
			}
		}
		scanner.close();
	}

}
