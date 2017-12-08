package com.hackerearth.hiring.capilary;

import java.util.Arrays;
import java.util.Scanner;

public class Dota2 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int Ms = sc.nextInt();
		int Mt = sc.nextInt();
		int Lt = sc.nextInt();
		int N = sc.nextInt();
		int[] points = new int[N];
		int maxPoint = 0;
		for (int i = 0; i < N; i++) {
			points[i] = sc.nextInt();
			if(maxPoint < points[i])
				maxPoint = points[i];
		}
		int[] min = new int[maxPoint+1];
		for (int i = 0; i < min.length; i++) {
			min[i] = -1;
		}
		int[] temp = new int[maxPoint+1];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = i;
		}
//		System.out.println(Arrays.toString(temp));
		findMinValue(min,points,Ms,Mt,Lt);
//		System.out.println(Arrays.toString(temp));
//		System.out.println(Arrays.toString(min));
		System.out.println(min[min.length-1]);
		sc.close();
	}

	private static void findMinValue(int[] min, int[] points, int ms, int mt, int lt) {
		Arrays.sort(points);
		// For first point whichever is minimum that will be min value.
		int i = 0;
		while (i < points[0]) {
			min[i] = 0;
			i++;
		}
		min[points[0]] = Math.min(ms, mt);
		// For second point onwards we will check value calculated based
		// on previous value.
		int k = points[0];
		for (i = 1; i < points.length; i++) {
//			System.out.println(Arrays.toString(min));
			while (k < points[i]) {
				min[k] = min[points[i-1]];
				k++;
			}			
			int index = points[i];
			int val1 = min[Math.max(0, index - 1)] + ms;
			int temp = 0;
			if (index - lt < 0) {
				temp = 0;
			}else{
				temp = min[index - lt];
			}
			int val2 = temp + mt;
//			System.out.println("val1="+val1+" val2="+val2);
			min[index] = Math.min(val1, val2);
			k = points[i];
		}
	}

	private static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			int midVal = a[mid];

			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else
				return mid; // key found
		}
		if (a[low] == key)
			return low;
		return low - 1; // key not found.
	}
}
