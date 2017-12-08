package com.samsung.test;

import java.util.Scanner;

public class GivingCoinsForCandies {

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        int T;
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            int num = sc.nextInt();
            int groups = sc.nextInt();
            long res = 1;
            if (num != groups) {
                int min = 1;
                int max = 2;
                int i = groups + 1;
                while (i <= num) {
                    res = (res / min) * max;
                    // System.out.println("i=" + i + " res=" + res);
                    if (i % groups == 0) {
                        min++;
                        max++;
                    }
                    i++;
                }
            }
            System.out.println("#" + test_case + " " + res);
        }
        sc.close();
    }
}


