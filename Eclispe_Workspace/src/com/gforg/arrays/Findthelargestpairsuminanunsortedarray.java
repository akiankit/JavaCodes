package com.gforg.arrays;

public class Findthelargestpairsuminanunsortedarray {

    public static void main(String[] args) {
        int[] ar = {10,38,10,6,0};
        System.out.println(largestPairSum(ar));
    }

    public static int largestPairSum(int[] ar) {
        int sum = 0;
        if (ar == null || ar.length == 0)
            return sum;
        if (ar.length == 1)
            return ar[0];
        if (ar.length == 2)
            return ar[0] + ar[1];
        int max = ar[0];
        int secondMax;
        if (ar[1] > max) {
            max = ar[1];
            secondMax = ar[0];
        } else {
            secondMax = ar[1];
        }
        for (int i = 2; i < ar.length; i++) {
            if (ar[i] < secondMax) {
                continue;
            } else if (ar[i] < max && ar[i] > secondMax) {
                secondMax = ar[i];
            } else if (ar[i] > max) {
                secondMax = max;
                max = ar[i];
            }
        }
        return secondMax + max;
    }
}
