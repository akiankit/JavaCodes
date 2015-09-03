package com.leetcode.arrays;

import java.util.LinkedList;
import java.util.List;

public class Combinations {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        System.out.println(combine(5,3));
        System.out.println("Time taken ="+(System.currentTimeMillis()-start));
    }

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> list = new LinkedList<List<Integer>>();
        int[] nums = new int[n];
        long power2= 1<<n;
        for(int i=0;i<n;i++)
            nums[i] = (i+1);
        for (int i = 1; i <=power2; i++) {
            List<Integer> tempList = new LinkedList<Integer>();
            long num = power2>>1;
            int count = 0;
            int bitIndex=0;
            while (num > 0) {
                long bit = (i/num)%2;
                if (bit == 1) {
                    tempList.add(nums[bitIndex]);
                    count++;
                };
                //sb.append(bit);
                num = num>>1;
                bitIndex++;
            }
            if (count == k)
                list.add(tempList);
        }
        return list;
    }

}
