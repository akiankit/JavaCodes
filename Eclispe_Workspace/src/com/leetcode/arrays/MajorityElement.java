package com.leetcode.arrays;

import java.util.LinkedList;
import java.util.List;

public class MajorityElement {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    // https://en.wikipedia.org/wiki/Boyer-Moore_Majority_Vote_Algorithm
    public int majorityElement(int[] nums) {
        int num = nums[0];
        int count = 0;
        for (Integer n : nums) {
            if (count == 0) {
                count = 1;
                num = n;
            } else if (num == n) {
                count++;
            } else {
                count--;
            }
        }
        return num;
    }
    
    public List<Integer> majorityElementII(int[] nums) {
        int y = 0, z = 1, county = 0, countz = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (num == y)
                county++;
            else if (num == z)
                countz++;
            else if (county == 0) {
                y = num;
                county = 1;
            } else if (countz == 0) {
                z = num;
                countz = 1;
            } else {
                county--;
                countz--;
            }
        }
        county = countz = 0;
        for (Integer num : nums) {
            if (num == y)
                county++;
            else if (num == z)
                countz++;
        }
        List<Integer> list = new LinkedList<Integer>();
        if (county > nums.length / 3)
            list.add(y);
        if (countz > nums.length / 3)
            list.add(z);
        return list;
    }

}
