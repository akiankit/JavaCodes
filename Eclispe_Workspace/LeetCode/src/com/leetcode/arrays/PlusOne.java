package com.leetcode.arrays;

import java.util.Arrays;

public class PlusOne {

    public static void main(String[] args) {
        int[] a = {};
        int[] one = plusOne(a);
        System.out.println(Arrays.toString(one));
    }
    
    static public int[] plusOne(int[] digits) {
        int carry = 1;
        boolean isSame = false;
        for (int i = digits.length-1; i >= 0; i--) {
            int temp = digits[i] + carry;
            digits[i] = temp % 10;
            carry = temp / 10;
            if (carry == 0) {
                isSame = true;
                break;
            }

        }
        if (isSame)
            return digits;
        else {
            int[] n = new int[digits.length + 1];
            n[0] = 1;
            for (int i = 1; i < n.length; i++) {
                n[i] = digits[i - 1];
            }
            return n;
        }
    }
}
