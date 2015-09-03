package com.leetcode.arrays;

import java.util.Arrays;

public class Sum3Closest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] num = {11, 1, -1, -4};
        int target = 3;
        System.out.println(threeSumClosest(num, target));
    }

    
    public static int threeSumClosest(int[] num, int target) {
        int sumClosest = 0;
        int diff = Integer.MAX_VALUE;
        Arrays.sort(num);
        for (int i = 0; i <= num.length - 3; i++) {
            int a = num[i];
            int j = i + 1;
            int k = num.length - 1;
            while (k > j && k > i) {
                int b = num[j];
                int c = num[k];
                int tempDiff = Math.abs(target - (a+b+c));
                if(tempDiff < diff){
                    diff = tempDiff;
                    sumClosest = a+b+c;
                }
                if (a + b + c == target) {
                    j++;
                    k--;
                    return sumClosest;
                } else if (a + b + c > target) {
                    k--;
                } else {
                    j++;
                }
            }
        }
        return sumClosest;
    }
}
