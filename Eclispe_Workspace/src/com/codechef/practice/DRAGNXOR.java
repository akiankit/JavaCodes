package com.codechef.practice;

import java.util.Scanner;

public class DRAGNXOR {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int numOfBits = scanner.nextInt();
			int numA = scanner.nextInt();
			int numB = scanner.nextInt();
			String binaryStringA = Integer.toBinaryString(numA);
			int onesCountA = getOnesCount(binaryStringA);
			String binaryStringB = Integer.toBinaryString(numB); 
			int onesCountB = getOnesCount(binaryStringB);
			int onesCountSum = onesCountA+onesCountB;
			if(numOfBits < onesCountSum){
				onesCountSum = numOfBits- (onesCountSum-numOfBits);
			}
			StringBuilder result = new StringBuilder();
			int j=0;
			for(;j<onesCountSum;j++){
				result.append(1);
			}
			for (; j < numOfBits; j++) {
				result.append(0);
			}
			int number = Integer.parseInt(result.toString(), 2);
			System.out.println(number);
		}
		scanner.close();
	}

	private static int getOnesCount(String binaryStringA) {
		int count = 0;
		for(int i=0;i<binaryStringA.length();i++){
			if(binaryStringA.charAt(i) =='1'){
				count++;
			}
		}
		return count;
	}

}
