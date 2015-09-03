
package com.leetcode.arrays;

import java.util.Arrays;


public class JumpGame {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {
                2, 3, 1, 1, 4
        };
        minJumps(a);
        System.out.println(canJump(a));
    }

    public static boolean canJump(int[] A) {
        return minJumpsFromEndToStart(A) != Integer.MAX_VALUE;
    }

    // Approach is dynamic programing specific
    public static int minJumps(int[] nums) {
        if (nums[0] == 0)
            return Integer.MAX_VALUE;
        else {
            int[] jumps = new int[nums.length];
            jumps[0] = 0;
            for (int i = 1; i < nums.length; i++) {
                jumps[i] = Integer.MAX_VALUE;
            }
            boolean found = false;
            for (int i = 0; i < nums.length && found == false; i++) {
                if(jumps[nums.length-1] ==1)
                    break;
                for (int j = i + 1; j < nums.length && j <= i + nums[i]; j++) {
                    jumps[j] = Math.min(jumps[j], jumps[i] + 1);
                    if(j==nums.length){
                        found = true;
                        break;
                    }
                }
            }
            System.out.println(Arrays.toString(jumps));
            return jumps[nums.length - 1];
        }
    }

    public static int minJumpsFromEndToStart(int[] a) {
        int length = a.length;
        int[] jumps = new int[length];
        jumps[length - 1] = 0;
        for (int i = length - 1; i >= 0; i--) {
            if (a[i] == 0) {
                jumps[i] = Integer.MAX_VALUE;
            } else if (length - i - 1 <= a[i]) {
                jumps[i] = 1;
            } else {
                int min = Integer.MAX_VALUE;
                for (int j = i + 1; j < length && j <= a[i] + i; j++) {
                    if (min > jumps[j]) {
                        min = jumps[j];
                    }
                }
                if (min != Integer.MAX_VALUE) {
                    jumps[i] = min + 1;
                } else {
                    jumps[i] = Integer.MAX_VALUE;
                }
            }
        }
        return jumps[0];
    }
}
