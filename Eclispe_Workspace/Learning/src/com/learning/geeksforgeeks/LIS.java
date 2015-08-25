package com.learning.geeksforgeeks;

import java.util.Arrays;

public class LIS {

	public static void main(String[] args) {
		int[] array = { 9, 10, 7, 4, 8, 5, 6 };
		System.out.println(findLISLenght(array));
		reverseArray(array);
	}

	static int findLISLenght(int[] array) {
		int length = 1;
		int[] lis = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < i; j++) {
				if (array[j] < array[i] && lis[j] + 1 > lis[i]) {
					lis[i] = lis[j] + 1;
					if (length < lis[i])
						length = lis[i];
				}
			}
		}
		System.out.println(Arrays.toString(lis));
		return length+1;
	}
	
	static private void reverseArray(int[] array){
		int n = array.length;
		for(int i=0;i<array.length/2;i++) {
			int temp = array[i];
			array[i] = array[n-i-1];
			array[n-i-1] = temp;
		}
		System.out.println(Arrays.toString(array));
	}
	
	static int findLDSLenght(int[] array) {
		int length = 1;
		int[] lis = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < i; j++) {
				if (array[j] < array[i] && lis[j] + 1 > lis[i]) {
					lis[i] = lis[j] + 1;
					if (length < lis[i])
						length = lis[i];
				}
			}
		}
		System.out.println(Arrays.toString(lis));
		return length+1;
	}
}
