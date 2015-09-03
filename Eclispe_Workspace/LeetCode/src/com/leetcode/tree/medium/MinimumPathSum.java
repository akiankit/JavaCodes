package com.leetcode.tree.medium;

public class MinimumPathSum {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[][] grid = {{1,2,3},{4,5,6}};
        System.out.println(minPathSum(grid));
    }

    static public int minPathSum(int[][] grid) {
        int sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 && j == 0)
                    sum = grid[0][0];
                else if (j == 0) {
                    sum = grid[i - 1][j] + grid[i][j];
                } else if (i == 0) {
                    sum = grid[i][j - 1] + grid[i][j];
                } else {
                    sum = grid[i][j] + Math.min(grid[i - 1][j], grid[i][j - 1]);
                }
                grid[i][j] = sum;
                System.out.print(sum+" ");
            }
            System.out.println();
        }
        return sum;
    }
}
