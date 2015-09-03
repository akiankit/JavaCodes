package com.leetcode.arrays;

public class MinSizeSubarraySum {

    public static void main(String[] args) {
        int[] nums = {2,1,3,5,2,4,3};
//        for(int i=0;i<20;i++)
            System.out.println("sum="+21+" MinWindow Length="+minSubArrayLen(20, nums));
    }

    static public int minSubArrayLen(int s, int[] nums) {
        int minWindow = 0;
        int start = 0, end = 0;
        int sum = 0;
        while (sum < s && end < nums.length) {
            sum = sum + nums[end];
            end++;
        }
        if (sum < s)
            return 0;
        while (sum >= s) {
            sum = sum - nums[start];
            start++;
        }
        minWindow = (end-1) -(start-1) +1;
        int tempStart = start;
        for (; end < nums.length;) {
            sum = sum + nums[end];
            while (sum >= s && tempStart <=end) {
                sum = sum - nums[tempStart];
                tempStart++;
            }
            int tempWindow = end - (tempStart-1)+1;
            if(minWindow > tempWindow) {
                minWindow = tempWindow;
                start = tempStart;
            }
            end++;
        }
//        System.out.print("window={");
//        for(int i=start-1;i<start-1+minWindow;i++)
//            System.out.print(nums[i] +",");
//        System.out.println("}");
        return minWindow;
    }
}
