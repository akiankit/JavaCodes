package com.leetcode.easy;

import java.util.HashMap;
import java.util.Map;

public class ContainsDuplicate {

    public static void main(String[] args) {
        int[] nums = {3,3};
        System.out.println(containsDuplicate(nums));
    }

    public static boolean containsDuplicate(int[] nums) {
        Map<Integer,Boolean> map = new HashMap<Integer, Boolean>();
        for(int i=0;i<nums.length;i++)
            if(map.containsKey(nums[i]))
                return true;
            else
                map.put(nums[i], true);
        return false;
    }
}
