
package com.leetcode.easy;

public class HouseRobber {

    public static void main(String[] args) {
        int[] num = {
                4
        };
//        System.out.println(rob(num));
        System.out.println(Math.max(rob(num, 0, num.length-2),rob(num,1,num.length-1)));
//        return Math.max(rob(num, 0, num.length-2),rob(num,1,num.length-1));
    }

    public static int rob(int[] num) {
        int maxMoney = 0;
        int numOfHouses = num.length;
        // If no house then no money
        if(numOfHouses == 0){
            return maxMoney;
        }
        if (numOfHouses == 1) {
            return num[0];
        } else if (numOfHouses == 2) {
            return Math.max(num[0], num[1]);
        } else {
            //If no. of house is greater than 2 then at every house check
            // for max money he can steal.
            // Max can be either at current house or prev house for which
            // Calculation is already done.
            int max1 = num[0];
            int max2 = Math.max(num[0],num[1]);
            for (int i = 2; i < numOfHouses; i ++) {
                if (max1 + num[i] > max2) {
                    max1 = max1 + num[i];
                } else {
                    max1 = max2;
                }
                i++;
                if (i < numOfHouses) {
                    if (max2 + num[i] > max1) {
                        max2 = max2 + num[i];
                    } else {
                        max2 = max1;
                    }
                }
            }
            maxMoney = Math.max(max1, max2);
        }
        return maxMoney;
    }
    
    // DP Approach
    public static int rob(int[] num, int start, int end) {
        if(start < end)
            return 0;
        if (start == end)// Length 1
            return num[start];
        if (start + 1 == end) // Length 2
            return Math.max(num[start], num[end]);
        // More than length 2
        int maxMoney[] = new int[end - start + 1];
        maxMoney[0] = num[start];
        maxMoney[1] = Math.max(num[start], num[start + 1]);
        for (int i = 2; i < maxMoney.length; i++) {
            maxMoney[i] = Math.max(maxMoney[i - 2] + num[start + i], maxMoney[i - 1]);
        }
        return maxMoney[maxMoney.length - 1];
    }
}
