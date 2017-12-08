package com.samsung.training;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Ranch {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[][] input = new char[100][100];
        for (int i = 0; i < 100; i++) {
            input[i] = br.readLine().toCharArray();
        }
        //print2Darray(input);
        changeGrid(input);
        print2Darray(input);
        System.out.println(numIslands(input));
    }
    
    private static void print2Darray(char[][] adjacenyMatrix) {
        System.out.println();
        for(int i=0;i<adjacenyMatrix.length;i++){
            System.out.println(Arrays.toString(adjacenyMatrix[i]));
        }
    }
    
    public static void changeGrid(char[][] grid) {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (grid[i][j] == '+') {
                    grid[i][j] = '0';
                } else {
                    grid[i][j] = '1';
                }
            }
        }
    }

    public static int numIslands(char[][] grid) {
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

    private static char[][] traverseForIsland(char[][] grid, int i, int j) {
        grid[i][j] = '2';
        if (isSafe(grid, i + 1, j))
            traverseForIsland(grid, i + 1, j);
        if (isSafe(grid, i, j + 1))
            traverseForIsland(grid, i, j + 1);
        if (isSafe(grid, i - 1, j))
            traverseForIsland(grid, i - 1, j);
        if (isSafe(grid, i, j - 1))
            traverseForIsland(grid, i, j - 1);
        if (isSafe(grid, i - 1, j + 1))
            traverseForIsland(grid, i - 1, j + 1);
        if (isSafe(grid, i + 1, j - 1))
            traverseForIsland(grid, i + 1, j - 1);
        if (isSafe(grid, i - 1, j - 1))
            traverseForIsland(grid, i - 1, j - 1);
        if (isSafe(grid, i + 1, j + 1))
            traverseForIsland(grid, i + 1, j + 1);
        return grid;
    }

    private static boolean isSafe(char[][] grid, int i, int j) {
        if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == '1')
            return true;
        return false;
    }

}
