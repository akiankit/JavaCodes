package com.hackerrank.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class Flowers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        long cost = 0;
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        long[] array = new long[N];
        for(int i=0;i<N;i++) {
            array[i] = scanner.nextLong();
            cost = cost + array[i];
        }
        if (K < N) {
            cost = getMinCost(array, N, K);
        }
        scanner.close();
        System.out.println(cost);
        
    }
    
    private static long getMinCost(long[] array, int N, int K) {
        int[] counts = new int[K];
        long cost = 0;
        int i = 0;
        int j = 0;
        Arrays.sort(array);
        reverseArray(array);
        while (i < N) {
            cost = cost + (counts[j % K] + 1) * array[i];
            counts[j % K]++;
            j++;
            i++;
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
