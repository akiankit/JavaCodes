package com.hackerrank.strings.problems;

import java.util.Scanner;

public class FibonacciFindingEasy {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int A = scanner.nextInt();
            int B = scanner.nextInt();
            int N = scanner.nextInt();
            System.out.println(fibonacci(N-1, A, B));
        }
        scanner.close();
    }
    
    private static long fibonacci(int n,int A, int B) {
        long mod = 1000000007;
        long[][] res = new long[2][2];
        long[][] a = new long[2][2];
        res[0][0] = 1;
        res[0][1] = 0;
        res[1][0] = 0;
        res[1][1] = 1;
        a[0][0] = 1;
        a[0][1] = 1;
        a[1][0] = 1;
        a[1][1] = 0;
        while (n != 0) {
            if ((n & 1) == 1) {
                res = multiplyMatrix(res, a, mod);
            }
            a = multiplyMatrix(a, a, mod);
            n = n >> 1;
        }
        long ans = (res[0][0] * B + res[0][1] * A) % mod;
        return ans;
    }

    private static long[][] multiplyMatrix(long[][] res, long[][] a, long mod) {
        long[][] ans = new long[2][2];
        ans[0][0] = (res[0][0] * a[0][0] + res[0][1] * a[1][0]) % mod;
        ans[0][1] = (res[0][0] * a[0][1] + res[0][1] * a[1][1]) % mod;
        ans[1][0] = (res[1][0] * a[0][0] + res[1][1] * a[1][0]) % mod;
        ans[1][1] = (res[1][0] * a[0][1] + res[1][1] * a[1][1]) % mod;
        return ans;
    }

}
