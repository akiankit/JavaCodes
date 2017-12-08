package com.hackerrank.strings.problems;

import java.util.Scanner;

public class SalaryBblues {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int q = scanner.nextInt();
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextLong();
        }
        long temp = 0;
        long lastGCD = 0;
        if(n==1)
            lastGCD = arr[0];
        else
            lastGCD = getGCD(arr);
        for (int i = 0; i < q; i++) {
            temp = scanner.nextInt();
            if (temp != 0) {
                for (int j = 0; j < n; j++) {
                    arr[j] += temp;
                }
                if (n == 1) {
                    lastGCD = arr[0];
                    System.out.println(arr[0]);
                } else {
                    lastGCD = getGCD(arr);
                    System.out.println(getGCD(arr));
                }
            } else {
                System.out.println(lastGCD);
            }

        }
        scanner.close();
    }
    
    static public long getGCdEuclid(long a, long b) {
        if (b == 0)
            return a;
        return getGCdEuclid(b, a % b);
    }
    
    private static long getGCD(long[] arr) {
        long gcd = getGCdEuclid(arr[0], arr[1]);
        for (int i = 2; i < arr.length; i++) {
            gcd = getGCdEuclid(gcd, arr[i]);
        }
        return gcd;
    }

}
