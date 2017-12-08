package com.leetcode.arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LongestConsecutiveSequence {

    public static void main(String[] args) {
        int[] nums = {100,1,200,3};
        System.out.println(longestConsecutive(nums));
    }

    public static int longestConsecutive(int[] nums) {
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        for (int integer : nums) {
            map.put(integer, true);
        }
        Set<Integer> keys = map.keySet();
        int maxLength = 1;
        for (Integer integer : keys) {
            // This will reduce a lot of call
            // Because we have checked for all consecutive elemetns
            // So if it's a consecutie element There is no need to check.
            if (map.containsKey(integer - 1))
                continue;
            integer++;
            int currentLength = 1;
            // Check for all consecutive elements present in array.
            while (map.containsKey(integer)) {
                currentLength++;
                integer++;
            }
            // If current length is greater than max = current
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }
        return maxLength;
    }
}
