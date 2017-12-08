package com.gforg.arrays;

import java.util.ArrayDeque;
import java.util.Deque;

// http://www.geeksforgeeks.org/maximum-of-all-subarrays-of-size-k/
public class MaximumOfAllSubarraysOfSizeK {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int arr[] = {8, 5, 10, 7, 9, 4, 15, 12, 90, 13};
        MaximumOfAllSubarraysOfSizeK max = new MaximumOfAllSubarraysOfSizeK();
        max.solve(arr, 4);
    }

    public void solve(int[] array, int k) {
        Deque<Integer> dque = new ArrayDeque<Integer>();
        int i=0;
        for (; i < k; i++) {
            // For very element, the previous smaller elements are useless so
            // remove them from Qi
            while ((!dque.isEmpty()) && array[i] >= array[dque.peekLast()])
                dque.pollLast();
            // Add new element at rear of queue
            dque.addLast(i);
        }
        for (; i < array.length; i++) {
            System.out.print(dque + " ");
            System.out.println(array[dque.peekFirst()] + " ");
            // Remove the elements which are out of this window
            while ((!dque.isEmpty()) && dque.peekFirst() <= i - k)
                dque.pollFirst(); // Remove from front of queue

            // Remove all elements smaller than the currently
            // being added element (remove useless elements)
            while ((!dque.isEmpty()) && array[i] >= array[dque.peekLast()])
                dque.pollLast();

            // Add current element at the rear of Qi
            dque.addLast(i);
        }
        System.out.println(dque);
        System.out.print(array[dque.peekFirst()]+" ");
    }
}
