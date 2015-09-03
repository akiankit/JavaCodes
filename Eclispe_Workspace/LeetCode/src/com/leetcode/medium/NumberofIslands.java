package com.leetcode.medium;

import java.util.Arrays;

// https://leetcode.com/problems/number-of-islands/
public class NumberofIslands {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String input = new String("11000 11000 00100 00011");
        String[] strings = input.split(" ");
        char grid[][] = new char[4][5];
//        System.out.println(input);
        for (int i = 0; i < 4; i++) {
                grid[i] = strings[i].toCharArray();
        }
        for (int j = 0; j < 4; j++) {
            System.out.println(Arrays.toString(grid[j]));
        }
        NumberofIslands num = new NumberofIslands();
        System.out.println(num.numIslands(grid));
        for (int j = 0; j < 4; j++) {
            System.out.println(Arrays.toString(grid[j]));
        }
    }
    
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0)
            return 0;
        int islands = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    islands++;
                    grid = traverseForIsland(grid, i, j);
                }
            }
        }
        return islands;
    }

    private char[][] traverseForIsland(char[][] grid, int i, int j) {
        grid[i][j] = '2';
        if (isSafe(grid, i + 1, j))
            traverseForIsland(grid, i + 1, j);
        if (isSafe(grid, i, j + 1))
            traverseForIsland(grid, i, j + 1);
        if (isSafe(grid, i - 1, j))
            traverseForIsland(grid, i - 1, j);
        if (isSafe(grid, i, j - 1))
            traverseForIsland(grid, i, j - 1);
        return grid;
    }

    private boolean isSafe(char[][] grid, int i, int j) {
        if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == '1')
            return true;
        return false;
    }

}
