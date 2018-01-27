package com.hackerrank.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

/**
 * <a href=
 * "https://www.hackerrank.com/challenges/hackerland-radio-transmitters">
 * Problem link</a><br>
 * 
 * <pre>
 * Idea is to always find the farthest house where transmitter can be put starting 
 * from begining. Once transmitter has been put on a house find out it's range and 
 * then start from next house in same way. In this way it is ensured that minimum 
 * number of tranmitters will be used.
 * </pre>
 */
public class Hackerland_Radio_Transmitters {

	static int hackerlandRadioTransmitters(int[] x, int k) {
		// Complete this function
		Arrays.sort(x);
		int houseIndex = 0;
		int count = 0;
		while (houseIndex < x.length) {
			int radioIndex = findNextIndexForRadio(x, houseIndex, k);
			houseIndex = findNextIndexForHouse(x, radioIndex, k);
			houseIndex = houseIndex + 1;
			count++;
		}
		return count;
	}

	private static int findNextIndexForHouse(int[] x, int radioIndex, int k) {
		int range = x[radioIndex] + k;
		int j = radioIndex;
		while (j < x.length && x[j] <= range) {
			radioIndex = j;
			j++;
		}
		return radioIndex;
	}

	private static int findNextIndexForRadio(int[] x, int i, int k) {
		int range = x[i] + k;
		int j = i;
		while (j < x.length && x[j] <= range) {
			i = j;
			j++;
		}
		return i;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int k = in.nextInt();
		int[] x = new int[n];
		for (int x_i = 0; x_i < n; x_i++) {
			x[x_i] = in.nextInt();
		}
		int result = hackerlandRadioTransmitters(x, k);
		System.out.println(result);
		in.close();
	}

}
