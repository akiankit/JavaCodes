package com.samsung.test;

import java.util.Scanner;

public class RouteFinding {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 10; i++) {
            int[][] paths = new int[2][100];
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 100; k++) {
                    paths[j][k] = -1;
                }
            }
            int testCase = scanner.nextInt();
            int roads = scanner.nextInt();
            for (int j = 0; j < roads; j++) {
                int start = scanner.nextInt();
                int end = scanner.nextInt();
                if (paths[0][start] == -1) {
                    paths[0][start] = end;
                } else {
                    paths[1][start] = end;
                }
            }
            /*for(int j=0;j<99;j++){
                System.out.println(Arrays.toString(paths[j]));
            }*/
            int start = 0;
            int end = 99;
            int path = pathExists(paths, start, end, 0) == true ? 1 : 0;
            System.out.println("#" + testCase + " " + path);
        }
        scanner.close();
    }
    
    private static boolean pathExists(int[][] paths, int start, int end, int index) {
        if (start == -1)
            return false;
        if (paths[0][start] == end || paths[1][start] == end)
            return true;
        else {
            return pathExists(paths, paths[0][start], end, index)
                    || pathExists(paths, paths[1][start], end, index);
        }
    }
}
