package com.hackerrank.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class MinimumAverageWaitingTime {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        Data[] array = new Data[count];
        for (int i = 0; i < count; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            array[i] = new Data(a, b);
        }
        scanner.close();
        Arrays.sort(array);
//        System.out.println(Arrays.toString(array));
        Minheap heap = new Minheap();
        long time = 0;
        long waitingTime = 0;
        int index = 0;
        heap.insert(array[index]);
        time = time + array[index].arrivalTime;
        index++;
        while (index != count) {
            Data min = heap.deleteMin();
            time = time + min.cookingTime;
            long tempWait = time - min.arrivalTime;
//            System.out.println("Serve Time is=" + time + " Arrival Time is="+min.arrivalTime+" Waiting Time for " + index + " is "
//                    + tempWait);
            waitingTime = waitingTime + tempWait;
            while (index < count && array[index].arrivalTime <= time) {
                heap.insert(array[index]);
                index++;
            }
            if (heap.size() == 0) {
                heap.insert(array[index]);
                index++;
            }
        }

        while (heap.size() != 0) {
            Data min = heap.deleteMin();
            time = time + min.cookingTime;
            long tempWait = time - min.arrivalTime;
//            System.out.println("Serve Time is=" + time + " Arrival Time is="+min.arrivalTime+" Waiting Time for " + index + " is "
//                    + tempWait);
            index++;
            waitingTime = waitingTime + tempWait;
        }
        System.out.println(waitingTime / count);
    }

}

class Minheap {
    private int count;

    private int capacity = 10;

    private Data[] array = new Data[capacity];

    public int size() {
        return count;
    }

    public Data getMin() {
        return array[0];
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<count;i++){
            sb.append(array[i]+",");
        }
        return sb.toString();
    }

    public void buildHeap(Data[] arr, int start, int end) {
        for (int i = start; i <= end; i++) {
            insert(arr[i]);
        }
    }

    public void heapifyDown(int i) {
        int l, r, min;
        Data temp;
        l = leftChild(i);
        r = rightChild(i);
        min = i;
        if (l != -1
                && ((/*array[l].arrivalTime + */array[l].cookingTime) < (/*array[i].arrivalTime + */array[i].cookingTime))) {
            min = l;
        }
        if (r != -1
                && ((/*array[r].arrivalTime + */array[r].cookingTime) < (/*array[min].arrivalTime +*/ array[min].cookingTime))) {
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
        int p, min;
        min = i;
        p = parent(i);
        if (p != -1
                && ((/*array[p].arrivalTime + */array[p].cookingTime) > (/*array[i].arrivalTime + */array[i].cookingTime))) {
            min = p;
        }
        if (min != i) {
            Data temp = array[i];
            array[i] = array[min];
            array[min] = temp;
            heapifyUp(min);
        }
    }

    public void insert(Data data) {
        if (count == capacity - 1) {
            resizeHeap();
        }
        count++;
        array[count - 1] = data;
        heapifyUp(count - 1);
    }

    public Data deleteMin() {
        Data min = array[0];
        array[0] = array[count - 1];
        count--;
        heapifyDown(0);
        return min;
    }

    private void resizeHeap() {
        Data[] temp = Arrays.copyOf(array, count);
        capacity = 2 * capacity;
        array = new Data[capacity];
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

class Data implements Comparable<Data> {

    long arrivalTime;
    long cookingTime;
    
    public Data(long t1, long t2) {
        this.arrivalTime = t1;
        this.cookingTime = t2;
    }
    
    @Override
    public String toString() {
        return arrivalTime+" "+cookingTime+" "+(arrivalTime+cookingTime);
    }
    
    @Override
    public int compareTo(Data data) {
        long a = this.arrivalTime;
        long b = data.arrivalTime;
        if (a == b)
            return 0;
        return a > b ? 1 : -1;
    }
    
}
