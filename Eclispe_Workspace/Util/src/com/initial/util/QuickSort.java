package com.initial.util;

public class QuickSort {
	private int[] numbers;
	private int number;

	public static void main(String[] args) {
		//int[] input = { 17, 12, 6, 19, 23, 8, 5, 10 };
		int[] input = {10,9,8,2,3,4,5,1,11,12};
		ArraysUtil.print1DArray(input);
		quickSort(input, 0, input.length - 1);
		ArraysUtil.print1DArray(input);
	}

	public static void quickSort(int[] input, int startIndex, int lastIndex) {
		if (startIndex < lastIndex) {
			int q = partition(input, startIndex, lastIndex);
			quickSort(input, startIndex, q);
			quickSort(input, q + 1, lastIndex);
		}
	}

	private static int partition(int[] input, int startIndex, int lastIndex) {
		//System.out.println("partition called with startIndex=" + startIndex + " lastIndex=" + lastIndex);
		int pivot = input[lastIndex];
		int i = startIndex;
		int j = lastIndex ;
		while (true) {
			while (input[j] > pivot) {
				j--;
			}
			while ( i< lastIndex &&  input[i] < pivot) {
				i++;
			}
			if (i <= j) {
				//System.out.println("Swapping " + input[i] + " and " + input[j]);
				int temp = input[i];
				input[i] = input[j];
				input[j] = temp;
				i++;j--;
			} else {
				//System.out.println("returning value of j="+j);
				return j;
			}
		}
	}
	
	public void sort(int[] values) {
		// Check for empty or null array
		if (values == null || values.length == 0) {
			return;
		}
		this.numbers = values;
		number = values.length;
		quicksort(0, number - 1);
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		int pivot = numbers[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (numbers[i] < pivot) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (numbers[j] > pivot) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private void exchange(int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}
}