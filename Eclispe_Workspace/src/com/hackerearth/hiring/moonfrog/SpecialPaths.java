package com.hackerearth.hiring.moonfrog;

import java.util.Scanner;

public class SpecialPaths {

    private static int n, m, k, mod = 1000007;
    private static boolean[][] mat, temp;
    private static int[][][] matrix;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();
        mat = new boolean[n][m];
        for (int i = 0; i < k; i++){
        	int row = in.nextInt();
        	int col = in.nextInt();
            mat[row-1][col-1] = true;
        }
        in.close();
        updateAllValues();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= k; i++)
            sb.append(matrix[0][0][i]).append(' ');
        System.out.println(sb.toString());
    }

    private static void updateAllValues() {
        temp = new boolean[n][m];
        matrix = new int[n][m][k + 1];

        temp[n - 1][m - 1] = true;
        if (mat[n - 1][m - 1])
            matrix[n - 1][m - 1][1] = 1;
        else
            matrix[n - 1][m - 1][0] = 1;
        updateValue(0, 0);
    }

    private static void updateValue(int row, int col) {
        if (row == n || col == m || temp[row][col])
            return;

        temp[row][col] = true;
        updateValue(row + 1, col);
        updateValue(row, col + 1);

        if (row != n - 1)
            for (int i = 0; i <= k; i++)
                matrix[row][col][i] = matrix[row + 1][col][i];

        if (col != m - 1)
            for (int i = 0; i <= k; i++)
                matrix[row][col][i] += matrix[row][col + 1][i];

        if (mat[row][col]) {
            for (int i = k; i >= 1; i--)
                matrix[row][col][i] = matrix[row][col][i - 1] % mod;

            matrix[row][col][0] = 0;
        } else
            for (int i = 0; i <= k; i++)
                matrix[row][col][i] %= mod;
    }

}

