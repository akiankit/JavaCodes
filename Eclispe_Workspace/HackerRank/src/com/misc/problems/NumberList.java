package com.misc.problems;

import java.util.Scanner;

public class NumberList {

    /**
     * {@link https://www.hackerrank.com/challenges/number-list}
     * @param args
     */
   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tests = sc.nextInt();
        for (int k = 0; k < tests; k++) {
            long n = sc.nextInt();
            int m = sc.nextInt();
            int num[] = new int[(int)n];
            for (int i = 0; i < n; i++) {
                num[i] = sc.nextInt();
            }
            long n1 = ((n * (n + 1)) / 2);
            long n2 = 0;
            int i=0;
            while (i < n) {
                if (num[i] > m) {
                    i++;
                    continue;
                }
                long count = 0;
                while (i < n && num[i] <= m) {
                    count++;
                    i++;
                }
                n2 += (count * (count + 1)) / 2;
            }
            long res = n1 - n2;
            System.out.println(res);
        }
        sc.close();
    }

}
