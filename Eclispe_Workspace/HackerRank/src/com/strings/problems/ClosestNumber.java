
package com.strings.problems;

import java.util.Scanner;

public class ClosestNumber {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int x = scanner.nextInt();
            long res = 0;
            if (b > 0) {
                long mod = calculateModPower(a, b, x);
                if (mod <= x / 2) {
                    res = calculatePower(a, b) - mod;
                } else {
                    res = calculatePower(a, b) + (x - mod);
                }
            }else if(b<0 && a==1)
                res = 1;
            System.out.println(res);
        }
        scanner.close();
    }

    static public long calculatePower(long a, long b) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) == 1)
                res = res * a;
            a = a * a;
            b = b >> 1;
        }
        return res;
    }

    static public long calculateModPower(long a, long b, long mod) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) == 1)
                res = (res * a) % mod;
            a = (a * a) % mod;
            b = b >> 1;
        }
        return res;
    }

}
