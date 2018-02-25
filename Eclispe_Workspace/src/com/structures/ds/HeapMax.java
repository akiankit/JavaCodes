package com.structures.ds;

import java.util.Arrays;

public class HeapMax {

	public static void main(String[] args) {
		// int[] arr = { 2, 2, 4, 3, 5, 7, 8, 10, 9, 3 };
		int[] arr = { 1, 2, 3, 4, 5 };
		Maxheap heap = new Maxheap();
		heap.buildHeap(arr, 0, arr.length - 1);
		System.out.println(heap);
		heap.insert(12);
		heap.insert(10);
		System.out.println(heap);
		System.out.println(heap.getMin());
		// System.out.println(heap);
		// System.out.println(heap.getMax());
		// System.out.println(heap);
		// System.out.println(heap.deleteMax());
		// System.out.println(heap);
		// heap.heapSort(arr);
		// for(int i=0;i<arr.length;i++) {
		// System.out.print(arr[i]+" ");
		// }
		// System.out.println();
	}

}

class Maxheap {

	private int count;

	private int capacity = 10;

	private int[] array = new int[capacity];

	public void heapSort(int[] arr, int start, int end) {
		Minheap heap = new Minheap();
		heap.buildHeap(arr, start, end);
		for (int i = end; i >= start; i--) {
			arr[i] = deleteMax();
		}
	}

	private int firstLeafIndex() {
		for (int i = 0; i < count; i++) {
			if (i * 2 + 1 > count - 1)
				return i;
		}
		return -1;
	}

	public int getMin() {
		int index = firstLeafIndex();
		int min = Integer.MAX_VALUE;
		for (int i = index; i < count; i++) {
			if (min > array[i])
				min = array[i];
		}
		return min;
	}

	/**
	 * Because it is a max heap, first index of array will always be max
	 * element.
	 *
	 * @return --Returns element at 0 index
	 */
	public int getMax() {
		return array[0];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < count - 1; i++) {
			sb.append(array[i] + ",");
		}
		sb.append(array[count - 1]);
		sb.append("]" + "\n");
		return sb.toString();
	}

	public void buildHeap(int[] arr, int start, int end) {
		for (int i = start; i <= end; i++) {
			insert(arr[i]);
		}
	}

	public void heapifyDown(int i) {
		int l, r, max;
		l = leftChild(i);
		r = rightChild(i);
		max = i;
		if (l != -1 && array[l] > array[i]) {
			max = l;
		}
		if (r != -1 && array[r] > array[max]) {
			max = r;
		}
		if (max != i) {
			swap(array, i, max);
			heapifyDown(max);
		}
	}

	/**
	 * Helper methods to swap two elements in array
	 *
	 * @param arr
	 *            -- Array in which elements needs to be swapped
	 * @param i
	 *            -- index of first element
	 * @param j
	 *            -- index of second element
	 */
	private void swap(int[] arr, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * While inserting a element in heap, Inserts elements at last position in
	 * array and keep moving in upward direction untill it is in correct
	 * position.</br>
	 * Idea is to compare the element with it's parent, If element is greater
	 * than it's parent element that means it is not in correct position; so
	 * swap the element with parent element and check if parent element is in
	 * correct position by comparing it's parent and so on untill we find the
	 * correct position of new element.
	 *
	 * @param i
	 *            -- Element index in array where element is inserted
	 */
	public void heapifyUp(int i) {
		int p;
		p = parent(i);
		if (p == -1) {
			return;
		}
		if (array[p] < array[i]) {
			swap(array, i, p);
			heapifyUp(p);
		}
		// if (max != i) {
		// temp = array[i];
		// array[i] = array[max];
		// array[max] = temp;
		//
		// }
	}

	/**
	 * Inserts a element in array such that after insertion also array maintains
	 * it's heap structure
	 *
	 * @param n
	 *            -- Number to be inserted in array
	 */
	public void insert(int n) {
		if (count == capacity - 1) {
			resizeHeap();
		}
		count++;
		array[count - 1] = n;
		heapifyUp(count - 1);
	}

	/**
	 * Swaps the first element(max element) with last element in heap. Then find
	 * correct position of the swapped element by heapifying down this element.
	 *
	 * @return max element deleted.
	 */
	public int deleteMax() {
		int max = array[0];
		array[0] = array[count - 1];
		count--;
		heapifyDown(0);
		return max;
	}

	private void resizeHeap() {
		int[] temp = Arrays.copyOf(array, count);
		capacity = 2 * capacity;
		array = new int[capacity];
		for (int i = 0; i < count; i++) {
			array[i] = temp[i];
		}
	}

	public int leftChild(int i) {
		if (i < 0)
			return -1;
		int child = 2 * i + 1;
		if (child > count - 1)
			return -1;
		return child;
	}

	public int rightChild(int i) {
		if (i < 0)
			return -1;
		int child = 2 * i + 2;
		if (child > count - 1)
			return -1;
		return child;
	}

	public int parent(int i) {
		if (i > count - 1 || i <= 0)
			return -1;
		return (i - 1) / 2;
	}
}
