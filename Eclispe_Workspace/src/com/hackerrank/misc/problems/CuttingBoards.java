package com.hackerrank.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class CuttingBoards {
    
    static long mod = 1000000007;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int M = scanner.nextInt();
            int N = scanner.nextInt();
            long[] y = new long[M-1];
            long[] x = new long[N-1];
            for(int j=0;j<M-1;j++) {
                y[j] = scanner.nextLong();
            }
            for(int j=0;j<N-1;j++) {
                x[j] = scanner.nextLong();
            }
            System.out.println(getMinCuttingCost(M, N, x, y));
        }
        scanner.close();
    }

    
    private static long getMinCuttingCost(int M, int N, long[] x, long[] y) {
        long cost = 0;
        Arrays.sort(x);
        Arrays.sort(y);
        reverseArray(x);
        reverseArray(y);
//        System.out.println(Arrays.toString(x));
//        System.out.println(Arrays.toString(y));
        int i = 0, j = 0, countX = 0, countY = 0;
        while (i < x.length && j < y.length) {
            if (x[i] > y[j]) {
                cost = (cost + x[i] * 1L * (countY + 1)) % mod;
                i++;
                countX++;
            } else {
                cost = (cost + y[j] * 1L * (countX + 1)) % mod;
                j++;
                countY++;
            }
        }
        while(i<x.length){
            cost = (cost + x[i] * 1L * (countY + 1)) % mod;
            i++;
            countX++;
        }
        while(j<y.length) {
            cost = (cost + y[j] * 1L * (countX + 1)) % mod;
            j++;
            countY++;
        }
        return cost;
    }
    
    private static void reverseArray(long[] array) {
        int n = array.length;
        for(int i=0;i<n/2;i++){
            long temp = array[i];
            array[i] = array[n-i-1];
            array[n-i-1] = temp;
        }
    }
}
