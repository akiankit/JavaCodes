package com.initial.util;

public class QuickSort {
	public static void main(String[] args) {
		//int[] input = { 17, 12, 6, 19, 23, 8, 5, 10 };
		int[] input = {10,9,8,2,3,4,5,1,11,12};
		ArraysUtil.print1DArray(input);
		quickSort(input, 0, input.length - 1);
		ArraysUtil.print1DArray(input);
	}

	public static void quickSort(int[] input, int low, int high) {
		if (low < high) {
			int pivot = partition(input, low, high);
			quickSort(input, low, pivot);
			quickSort(input, pivot + 1, high);
		}
	}

	private static int partition(int[] input, int low, int high) {
		int left,right,pivotItem=input[low];
		left = low;
		right = high;
		while(left<right){
			while(input[left]<=pivotItem)
				left++;
			while(input[right]>pivotItem)
				right--;
			if(left<right){
				int temp = input[left];
				input[left] = input[right];
				input[right] = temp;
			}
		}
		input[low] = input[right];
		input[right] = pivotItem;
		return right;
	}
	
}