package com.samsung.test;

import java.util.Scanner;

public class Maze2 {
    
    static int N = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 10; i++) {
            int testCase = scanner.nextInt();
            int[][] array = new int[N][N];
            for (int j = 0; j < N; j++) {
                String line = scanner.next();
                for (int k = 0; k < line.length(); k++) {
                    array[j][k] = Integer.parseInt("" + line.charAt(k));
                }
            }
            
            int j = 0, k = 0;
            /*for (j = 0; j < N; j++) {
                System.out.println(Arrays.toString(array[j]));
            }*/
            j=0;
            while (array[j][k] != 2) {
                k++;
                if (k == N) {
                    k = 0;
                    j++;
                }

            }
//            System.out.println("For 2: j=" + j + " k=" + k);
            array = is3Reachable(j, k, array);
            /*for (j = 0; j < N; j++) {
                System.out.println(Arrays.toString(array[j]));
            }*/
            j = 0;
            k = 0;
            while (j < N && array[j][k] != 3) {
                k++;
                if (k == N) {
                    k = 0;
                    j++;
                }
            }
            
//            System.out.println("For 3: j=" + j + " k=" + k);
            int answer = 1;
            if (j < N && k < N && array[j][k] == 3)
                answer = 0;
            System.out.println("#" + testCase + " " + answer);
        }
        scanner.close();
    }

    private static boolean isSafe(int[][] array, int i, int j) {
        if (i < N && i >= 0 && j >= 0 && j < N && (array[i][j] == 0 || array[i][j] == 3))
            return true;
        return false;
    }

    private static int[][] is3Reachable(int j, int k, int[][] array) {
        array[j][k] = 2;
        if (isSafe(array, j + 1, k))
            is3Reachable(j + 1, k, array);
        if (isSafe(array, j - 1, k))
            is3Reachable(j - 1, k, array);
        if (isSafe(array, j, k + 1))
            is3Reachable(j, k + 1, array);
        if (isSafe(array, j, k - 1))
            is3Reachable(j, k - 1, array);
        return array;
    }

}
