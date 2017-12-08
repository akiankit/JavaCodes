package com.leetcode.medium;

public class POWER {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        System.out.println(myPow(2, 2));
//        System.out.println(myPow(2, 4));
//        System.out.println(myPow(2, 6));
//        System.out.println(myPow(2, 8));
//        System.out.println(myPow(2, 10));
//        System.out.println(myPow(2, 12));
//        System.out.println(myPow(2, 14));
//        System.out.println(myPow(2, 16));
//        System.out.println(myPow(2, 18));
        System.out.println(myPow(1.000, -2147483648));
        
    }

    public static double myPow(double x, int n) {
        if(x == 1)
            return 1;
        if(x == -1) {
            return ((n&1) ==0 ? 1: -1);
        }
        if (n < 0)
            return myPow(1/x, Math.abs(n));
        if (n == 0 )
            return 1;
        if (n == 1)
            return x;
        if (n == 2)
            return x * x;
        if ((n & 1) == 0) {
            return myPow(myPow(x, n / 2), 2);
        } else {
            return myPow(myPow(x, n / 2), 2) * x;
        }
    }
}
