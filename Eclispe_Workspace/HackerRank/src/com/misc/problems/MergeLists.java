package com.misc.problems;

import java.util.Scanner;

public class MergeLists {
    
    static long[][] coefficients;
    
    static {
        int size = 210;
        long mod = (long)(Math.pow(10,9)+7);
        
        coefficients = new long[size+1][];
        // Initailizing the values for (1,0) and (1,1)
        coefficients[0] = new long[1];
        coefficients[1] = new long[] {
                1, 1
        };
        int lastArraySize = 0;
        for (int i = 2; i <= size; i++) {
            // I is starting from 0. So possible coefficients for 'i' is 'i+2'
            int arraySize = i + 1;
            coefficients[i] = new long[arraySize];
            // for any case coefficient of all (n,0) will be 1;
            coefficients[i][0] = 1;
            int j = 1;
            for (; j < (arraySize + 1) / 2; j++) {
                if (j == lastArraySize && j == arraySize - 1)
                    coefficients[i][j] = (2 * coefficients[i - 1][j - 1]) % mod;
                else
                    coefficients[i][j] = (coefficients[i - 1][j - 1] + coefficients[i - 1][j])
                            % mod;
            }
            int k = j;
            if ((i & 1) == 0) {
                j--;
                k = j;
            }
            for (; k < arraySize && j >= 0; k++, --j) {
                if ((i & 1) != 0) {
                    coefficients[i][k] = coefficients[i][j - 1];
                } else {
                    coefficients[i][k] = coefficients[i][j];
                }
            }
            lastArraySize = arraySize;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tests = sc.nextInt();
        for(int i=0;i<tests;i++) {
            int N = sc.nextInt();
            int M = sc.nextInt();
            if(N < M){
                int temp = N;
                N = M;
                M = temp;
            }
            System.out.println(coefficients[N+M][M]);
        }
        sc.close();
    }

}
