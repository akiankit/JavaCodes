package com.samsung.training;

import java.util.Arrays;
import java.util.Scanner;

public class TopologicalSort {

    
    // DFS method can also be used after reversing edges.
    // TODO try that method also.
    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        int T;
        T = 10;
        for (int test_case = 1; test_case <= T; test_case++) {
            int n = sc.nextInt();
            int e = sc.nextInt();
            int[][] adjacenyMatrix = new int[n + 1][n + 1];
            for (int i = 0; i < e; i++) {
                int start = sc.nextInt();
                int end = sc.nextInt();
                adjacenyMatrix[start][end] = 1;
            }
            int[] inDegree = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                int count = 0;
                for (int j = 1; j <= n; j++) {
                    if (adjacenyMatrix[j][i] == 1)
                        count++;
                }
                inDegree[i] = count;
            }
            StringBuilder sb = new StringBuilder(n);
            for (int i = 1; i <= n; i++) {
                boolean found = false;
                int j = 1;
                for (; j <= n; j++) {
                    if (inDegree[j] == 0) {
                        sb.append(j + " ");
                        inDegree[j] = -1;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    for (int k = 1; k <= n; k++) {
                        if (adjacenyMatrix[j][k] == 1) {
                            inDegree[k]--;
                        }
                    }
                }
            }
            System.out.println("#" + test_case + " " + sb.toString().trim());
        }
        sc.close();
    }

    private static void print2Darray(int[][] adjacenyMatrix) {
        System.out.println();
        for(int i=0;i<adjacenyMatrix.length;i++){
            System.out.println(Arrays.toString(adjacenyMatrix[i]));
        }
    }

}
