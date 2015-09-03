package com.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class MaxMin {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        int K = scanner.nextInt();
        int[] array = new int[count];
        for (int j = 0; j < count; j++) {
            array[j] = scanner.nextInt();
        }
        System.out.println(getMinUnfairness(array, K));
        scanner.close();
    }
    
    private static int getMinUnfairness(int[] array, int K) {
        int min = Integer.MAX_VALUE;
        int i = 0, j = K - 1;
        Arrays.sort(array);
        for (; j < array.length; j++, i++) {
            int unfairness = array[j] - array[i];
            if (min > unfairness) {
                min = unfairness;
            }
        }
        return min;
    }

}
