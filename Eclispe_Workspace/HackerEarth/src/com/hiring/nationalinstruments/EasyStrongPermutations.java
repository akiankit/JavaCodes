package com.hiring.nationalinstruments;

import java.util.Arrays;
import java.util.Scanner;

public class EasyStrongPermutations {

	public static void main(String[] args) {
		int h = 8;
		   int b = h++ + h++ + h++;
		   System.out.println(h);
		
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] arr = readIntArray(sc, n);
		Arrays.sort(arr);
		arr = reArrangeArray(arr);
//		System.out.println(Arrays.toString(arr));
		System.out.println(getStrength(arr));
		sc.close();
	}
	
	private static int[] reArrangeArray(int[] arr) {
//		System.out.println(Arrays.toString(arr));
		int[] arr1 = new int[arr.length];
		int n = arr.length;
		int i = 0, j = n - 1;
		int k = 0;
		for (; i < n / 2; k += 2) {
			arr1[k] = arr[i];
			arr1[k + 1] = arr[j];
			i++;
			j--;
		}
		if ((n & 1) == 1) {
			arr1[k] = arr[i];
		}
//		System.out.println(Arrays.toString(arr1));
		return arr1;
	}
	
	private static long getStrength(int[] arr){
		int n = arr.length;
		long strength = Math.abs(arr[n-1] - arr[0]);
		for(int i=1;i<n;i++){
			strength += Math.abs(arr[i] - arr[i - 1]);
		}
		return strength;
	}

	private static int[] readIntArray(Scanner sc, int n) {
		int[] arr = new int[n];
		for (int i = 0; i < arr.length; i++)
			arr[i] = sc.nextInt();
		return arr;
	}
}
