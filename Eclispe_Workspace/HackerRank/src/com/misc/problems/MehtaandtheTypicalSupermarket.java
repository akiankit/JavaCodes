package com.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class MehtaandtheTypicalSupermarket {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] coins = new int[n];
        for (int i = 0; i < n; i++) {
            coins[i] = sc.nextInt();
        }
        int queries = sc.nextInt();
        for (int i = 0; i < queries; i++) {
            long L = sc.nextLong();
            long R = sc.nextLong();
            System.out.println(getTotalCount2(coins, L, R));
        }
        sc.close();
    }

    private static long getTotalCount2(int[] coins, long L, long R) {
        Arrays.sort(coins);
        if (coins[0] == 1) {

        }
        long count = 0;
        for (long j = L; j <= R; j++) {
            for (int i = 0; i < coins.length; i++) {
                if (j % coins[i] == 0) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
