package com.leetcode.easy;

import java.util.HashMap;
import java.util.Map;

public class ContainsDuplicateII {

    public static void main(String[] args) {

    }
    
    static public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                if (Math.abs(map.get(nums[i]) - i) <= k)
                    return true;
            }
            map.put(nums[i], i);
        }
        return false;
    }
}
