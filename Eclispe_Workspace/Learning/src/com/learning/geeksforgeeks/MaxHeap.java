package com.learning.geeksforgeeks;

import java.util.Arrays;

class Maxheap {
    private int count;

    private int capacity = 10;

    private int[] array = new int[capacity];

    public void heapSort(int[] arr,int start, int end) {
        Maxheap heap = new Maxheap();
        heap.buildHeap(arr,start,end);
        for (int i = end; i >= start; i--) {
            arr[i] = deleteMax();
        }
    }
    
    private int firstLeafIndex() {
        for(int i=0;i<count;i++) {
            if(i*2+1 > count-1)
                return i;
        }
        return -1;
    } 
    
    public int getMin() {
        int index = firstLeafIndex();
        int min = Integer.MAX_VALUE;
        for(int i = index;i<count;i++) {
            if(min > array[i])
                min = array[i];
        }
        return min;
    }

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
        sb.append(array[count-1]);
        sb.append("]"+"\n");
        return sb.toString();
    }

    public void buildHeap(int[] arr,int start,int end) {
        for (int i = start; i <= end; i++) {
            insert(arr[i]);
        }
    }

    // Max Heap
    public void heapifyDown(int i) {
        int l, r, max, temp;
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
            temp = array[i];
            array[i] = array[max];
            array[max] = temp;
            heapifyDown(max);
        }
    }

    public void heapifyUp(int i) {
        int p, max, temp;
        max = i;
        p = parent(i);
        if (p != -1 && array[p] < array[i]) {
            max = p;
        }
        if (max != i) {
            temp = array[i];
            array[i] = array[max];
            array[max] = temp;
            heapifyUp(max);
        }
    }

    public void insert(int n) {
        if (count == capacity - 1) {
            resizeHeap();
        }
        count++;
        array[count - 1] = n;
        heapifyUp(count - 1);
    }

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