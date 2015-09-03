package com.structures.ds;

import java.util.Arrays;

public class HeapMin {

    public static void main(String[] args) {
        int[] arr = {2,2,4,3,5,7,8,10,9,3};
        Minheap heap = new Minheap();
        heap.buildHeap(arr,0,arr.length-1);
        System.out.println(heap);
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMax());
//        System.out.println(heap);
//        heap.heapSort(arr);
//        for(int i=0;i<arr.length;i++) {
//            System.out.print(arr[i]+" ");
//        }
        System.out.println();
    }

}

class Minheap {
    private int count;

    private int capacity = 10;

    private int[] array = new int[capacity];

    public void heapSort(int[] arr,int start, int end) {
        Minheap heap = new Minheap();
        heap.buildHeap(arr,start,end);
        for (int i = end; i >= start; i--) {
            arr[i] = deleteMin();
        }
    }
    
    private int firstLeafIndex() {
        for(int i=0;i<count;i++) {
            if(i*2+1 > count-1)
                return i;
        }
        return -1;
    } 
    
    public int getMax() {
        int index = firstLeafIndex();
        int max = Integer.MIN_VALUE;
        for(int i = index;i<count;i++) {
            if(max < array[i])
                max = array[i];
        }
        return max;
    }

    public int getMin() {
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
        int l, r, min, temp;
        l = leftChild(i);
        r = rightChild(i);
        min = i;
        if (l != -1 && array[l] < array[i]) {
            min = l;
        }
        if (r != -1 && array[r] < array[min]) {
            min = r;
        }
        if (min != i) {
            temp = array[i];
            array[i] = array[min];
            array[min] = temp;
            heapifyDown(min);
        }
    }

    public void heapifyUp(int i) {
        int p, min, temp;
        min = i;
        p = parent(i);
        if (p != -1 && array[p] > array[i]) {
            min = p;
        }
        if (min != i) {
            temp = array[i];
            array[i] = array[min];
            array[min] = temp;
            heapifyUp(min);
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

    public int deleteMin() {
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
