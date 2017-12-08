package com.hackerrank.misc.problems;

import java.util.Scanner;

public class AkhilAndGF {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tests = sc.nextInt();
        for (int i = 0; i < tests; i++) {
            long N = sc.nextLong();
            int mod = sc.nextInt();
            System.out.println(getBigMod(mod, 1, N));
        }
        sc.close();
    }

    static public int getBigMod(int mod, int digit, long N) {
        int first = 0;
        int last = 0;
        int count = 0;
        int res = 0;
        for (long i = 1; i <= N; i++) {
            res = (res * 10 + 1) % mod;
            if (first == 0) {
                first = res;
            } else if (first != 0 && first == res) {
                N = N % count;
                break;
            } else if (last == res) {
                return res;
            }
            count++;
            last = res;
        }
        res = 0;
        for (long i = 1; i <= N; i++)
            res = (res * 10 + 1) % mod;
        return res;
    }
}
