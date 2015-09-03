package com.leetcode.arrays;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class SlidingWindowMaximum {

    public static void main(String[] args) {
        int[] nums = {1,3,-1,-3,5,3,6,7};
        int k = 2;
        SlidingWindowMaximum window = new SlidingWindowMaximum();
        System.out.println(Arrays.toString(window.maxSlidingWindow(nums, k)));
    }
    
    // This is O(n) complexity solution. This concept I have read from Narshimha
    public int[] maxSlidingWindow(int[] nums, int k) {
        int[] sliding = new int[nums.length - k + 1];
        if (nums == null || nums.length == 0){
            sliding = new int[0];
            return sliding;
        }
        if(k==1)
            return nums;
        Deque<Integer> queue = new ArrayDeque<Integer>();
        for (int i = 0; i < k; i++) {
            int num = nums[i];
            while (!queue.isEmpty() && nums[queue.peekLast()] < num) {
                queue.pollLast();
            }
            queue.addLast(i);
        }
        sliding[0] = nums[queue.peekFirst()];
//        System.out.println(queue);
        for (int i = k; i < nums.length; i++) {
//            System.out.println("index i=" + i);
            int num = nums[i];
            int startIndex = i - k + 1;
            while (!queue.isEmpty() && queue.peekFirst() < startIndex) {
                queue.removeFirst();
            }
            while (!queue.isEmpty() && nums[queue.peekLast()] < num) {
                queue.removeLast();
            }
            queue.addLast(i);
            sliding[i - k+1] = nums[queue.peekFirst()];
//            System.out.println(queue);
        }
        return sliding;
    }

}
