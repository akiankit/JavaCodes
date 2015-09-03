package com.codechef.practice;

import java.util.Scanner;

public class CIELRCPT {
	
	static private int reciepiesCount = 12;
	static  private int a[] =new int[reciepiesCount]; 
	
	static{
		for(int i=0;i<reciepiesCount;i++){
			a[i] = (int) Math.pow(2, i);
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for(int i=0;i<testCases;i++){
			int nextNum = scanner.nextInt();
			int orders = 0;
			while(nextNum > 0){
				int temp = findLessOrEqual(nextNum);
				if(nextNum%temp ==0){
					orders = orders + nextNum/temp;
					nextNum = 0;
				}else{
					orders ++;
					nextNum = nextNum - temp;
				}
				
			}
			System.out.println(orders);
		}
		scanner.close();
	}

	private static int findLessOrEqual(int nextNum) {
		int num = 1;
		int k=0;
		while(k < reciepiesCount && a[k] <=nextNum){
			num = a[k];
			k++;
		}
		return num;
	}
}
