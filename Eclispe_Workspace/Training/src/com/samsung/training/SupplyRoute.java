package com.samsung.training;

import java.util.Arrays;
import java.util.Scanner;

public class SupplyRoute {/*

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        int T;
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            int n = sc.nextInt();
            int[][] map = new int[n][n];
            for(int i=0;i<n;i++) {
                String line = sc.next();
                for(int j=0;j<n;j++) {
                    map[i][j] = line.charAt(j)-'0';
                }
            }
            int cost = findMinPath(map);
            System.out.println("#" + test_case+" "+cost);
        }
        sc.close();
    }
    
    
    private static boolean isSafe(int i, int j, int n) {
        if (i >= 0 && i < n && j >= 0 && j < n)
            return true;
        return false;
    }

    private static int findMinPath(int[][] map) {
        int n = map.length;
        int[][] weight = new int[n][n];
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                weight[i][j] = Integer.MAX_VALUE;
            }
        }
        PointQueue queue = new PointQueue(n * n);
        weight[0][0] = map[0][0];
        queue.enqueue(new Point(0, 0));
        while (!queue.isEmpty()) {
            Point temp = queue.dequeue();
            if (temp == null)
                break;
            int row = temp.row;
            int col = temp.col;
            if (isSafe(row + 1, col, n)) {
                int original = weight[row + 1][col];
                weight[row + 1][col] = Math.min(weight[row + 1][col], weight[row][col]
                        + map[row + 1][col]);
                if (original != weight[row + 1][col])
                    queue.enqueue(new Point(row + 1, col));
            }
            if (isSafe(row - 1, col, n)) {
                int original = weight[row - 1][col];
                weight[row - 1][col] = Math.min(weight[row - 1][col], weight[row][col]
                        + map[row - 1][col]);
                if (original != weight[row - 1][col])
                    queue.enqueue(new Point(row - 1, col));
            }
            if (isSafe(row, col + 1, n)) {
                int original = weight[row][col + 1];
                weight[row][col + 1] = Math.min(weight[row][col + 1], weight[row][col]
                        + map[row][col + 1]);
                if (original != weight[row][col + 1])
                    queue.enqueue(new Point(row, col + 1));
            }
            if (isSafe(row, col - 1, n)) {
                int original = weight[row][col - 1];
                weight[row][col - 1] = Math.min(weight[row][col - 1], weight[row][col]
                        + map[row][col - 1]);
                if (original != weight[row][col - 1])
                    queue.enqueue(new Point(row, col - 1));
            }
        }
        return weight[n-1][n-1];
    }
*/}

/*class Point {
    public Point(int i, int j) {
        row = i;
        col = j;
    }
    int row;
    int col;
    
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}

class PointQueue {
    Point[] arr = null;

    int top = 0;

    int start = -1;

    int size = 0;

    public boolean isEmpty() {
        return size == 0;
    }

    public PointQueue(int length) {
        arr = new Point[10000];
    }

    public void enqueue(Point val) {
        if (!isEmpty() && top == start)
            return;
        arr[top] = val;
        top = (top + 1) % 10000;
        if(start == -1)
            start = 0;
        size++;
    }

    public Point dequeue() {
        if(isEmpty())
            return null;
        Point temp = arr[start];
        start = (start + 1) % 10000;
        size--;
        return temp;
    }

    @Override
    public String toString() {
        return Arrays.toString(arr);
    }
}*/