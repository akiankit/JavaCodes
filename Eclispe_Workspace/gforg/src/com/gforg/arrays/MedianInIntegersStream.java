
package com.gforg.arrays;

import java.util.Arrays;

public class MedianInIntegersStream {

    static Maxheap max = new Maxheap();
    static Minheap min = new Minheap();
    static int length = 0;
    
    public static void main(String[] args) {
        int[] a  = {1,2,3,4,5,6,7,8,9,10};
        for(int i=0;i<a.length;i++) {
            insert(a[i]);
        }
    }

    
    /**
     * a and b will be equal if length is odd otherwise different.
     * @param a
     * @param b
     * median will be shown on calling this function.
     */
    private static void getMedian(int a, int b) {
//        System.out.print("maxHeap="+max);
//        System.out.print("minheap="+min);
        double median = ((double)a + (double)b) / (double)2;
        median = Math.round(median * 10.0) / 10.0;
        System.out.println(median);
//        System.out.println();
    }
    
    /**
     * Here logic is little out of box. My logic is as below:
     * Hmax is for 1st half of sorted numbers and Hmin is for second half of sorted numbers.
     * Basically we want to find max in first half and min in second half and average will be 
     * median if length is even. Otherwise min in second half will be median.
     *  
     * Height of Hmin(either n or n+1) >=Height of Hmax(always n) 
     * There are 3 cases when any new number appears:
     * Case No.1: n<= top of Hmax
     * Case No.2: top of Hmin > n > top of Hmax
     * Case No.3: n >= top of Hmin
     * Now when length is odd:
     * Case 1:
     * insert n in Hmax and median = (TopHmax + TopHmin)/2
     * Case 2:
     * insert n in Hmax and median = (TopHmax + TopHmin)/2
     * Case 3:
     * Insert n in Hmin and fetch HminTop and insert it in Hmax and then median = (TopHmax + TopHmin)/2
     * 
     * When Length is even:
     * Case 1:
     * insert n in Hmax and fetch HmaxTop and insert it in Hmin and  then median = (TopHmin + TopHmin)/2
     * Case 2 and 3:
     * insert n in Hmin and  then median = (TopHmin + TopHmin)/2
     * {@link http://codercareer.blogspot.in/2012/01/no-30-median-in-stream.html}
     * 
     * @param n
     */
    private static void insert(int n) {
//        System.out.println("insert="+n);
        if ((length & 1) == 0) {
            if (max.size() > 0 && n <= max.getMax()) {
                max.insert(n);
                n = max.deleteMax();
            }
            min.insert(n);
            getMedian(min.getMin(), min.getMin());
        } else {
            if (min.size() > 0 && n >= min.getMin()) {
                min.insert(n);
                n = min.deleteMin();
            }
            max.insert(n);
            getMedian(max.getMax(), min.getMin());
        }
        length++;
    }
}

class Minheap {
    private int count;

    private int capacity = 10;

    private int[] array = new int[capacity];
    
    public int size() {
        return count;
    }

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
        if (size() > 0)
            sb.append(array[count - 1]);
        sb.append("]" + "\n");
        return sb.toString();
    }

    public void buildHeap(int[] arr,int start,int end) {
        for (int i = start; i <= end; i++) {
            insert(arr[i]);
        }
    }

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
        if (p != -1 && array[p] < array[i]) {
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

class Maxheap {
    private int count;

    private int capacity = 10;

    private int[] array = new int[capacity];

    public int size() {
        return count;
    }

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
        if (size() > 0)
            sb.append(array[count - 1]);
        sb.append("]" + "\n");
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