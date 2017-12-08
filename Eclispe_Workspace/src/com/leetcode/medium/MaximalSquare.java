package com.leetcode.medium;

public class MaximalSquare {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String input = new String("10100 10111 11111 10010");
        String[] strings = input.split(" ");
        char grid[][] = new char[4][5];
//        System.out.println(input);
        for (int i = 0; i < 4; i++) {
//            System.out.println(strings[i]);
                grid[i] = strings[i].toCharArray();
        }
        MaximalSquare  max = new MaximalSquare();
        char grid1[][] = {{'1'}};
        System.out.println(max.maximalSquare(grid1));

    }

    // DP Approach
    public int maximalSquare(char[][] matrix) {
        if(matrix == null || matrix.length == 0)
            return 0;
        int[][] mat = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                mat[i][j] = matrix[i][j] - '0';
            }
        }
        int res = 0;
        for (int i = 0; i < matrix[0].length; i++) {
            if (mat[0][i] == 1) {
                res = 1;
                break;
            }
        }
        if (res == 0) {
            for (int i = 0; i < matrix.length; i++) {
                if (mat[i][0] == 1) {
                    res = 1;
                    break;
                }
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (mat[i][j] == 1) {
                    mat[i][j] = Math.min(mat[i - 1][j - 1], Math.min(mat[i - 1][j], mat[i][j - 1])) + 1;
                    if (mat[i][j] > res)
                        res = mat[i][j];
                }
            }
        }
        return res*res;
    }
}
