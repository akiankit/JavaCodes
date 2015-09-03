package com.hiring.superprof;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SamuAndHerBirthdayParty {
	
	static List<String> sets = new LinkedList<String>();

	static {
		for (int i = 1; i <= 1 << 10; i++) {
			sets.add(Integer.toBinaryString(i));
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int tests = scanner.nextInt();
		for (int i = 0; i < tests; i++) {
			int N = scanner.nextInt();
			int K = scanner.nextInt();
			String likes[] = new String[N];
			int dishes[] = new int[K];
			for(int j=K-1;j>=0;j--){
				dishes[j] = j;
			}
			
		}
		scanner.close();
	}
	
	private static int getMinDishCount(String likes[], int N, int[] dishes) {
		for (String string : sets) {
			
		}
		return 0;
	}

}
