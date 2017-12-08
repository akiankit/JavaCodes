package com.leetcode.easy;

import java.util.Scanner;

public class NumberOf1Bits {

    public static void main(String[] args) {
        /*for(int i=1;i<=20;i++){
            System.out.println("Number = "+i+"Binary Represetntation = "+Integer.toBinaryString(i)+"HammingWeight="+hammingWeight(i));
        }*/
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();
        System.out.println(hammingWeight(n));
    }

    public static int hammingWeight(int n) {
        int numOf1s = 0;
        while (n >= 2) {
            if (n % 2 == 1)
                numOf1s++;
            n = n / 2;
        }
        if (n == 1)
            numOf1s++;
        return numOf1s;
    }
}
