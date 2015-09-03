package com.leetcode.arrays;

public class SearchInA2DMatrix {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[][] matrix = {
                /*{1,   4,  7, 11, 15},
                {2,   5,  8, 12, 19},
                {3,   6,  9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}*/
                {5,6,9},
                {9,10,11},
                {11,14,18}
        };
        SearchInA2DMatrix searchInA2DMatrix = new SearchInA2DMatrix();
        System.out.println(searchInA2DMatrix.searchMatrix(matrix, 9));
//        for (int i = 0; i <= 100; i++) {
//            System.out.println(searchInA2DMatrix.searchMatrix(matrix, i));
//        }
    }
    
    public boolean searchMatrix(int[][] matrix, int target) {
        return searchMatrix(matrix, target, 0, matrix.length - 1, 0, matrix[0].length - 1);
    }

    private boolean searchMatrix(int[][] matrix, int target, int sr, int er, int sc, int ec) {
        // Finding all the terminating conditions in one shot is difficult. 
        // I was able to find these using failing test cases.
        if(sr>er || sc>ec || sr<0 || sc<0)
            return false;
        if(target < matrix[sr][sc] || target > matrix[er][ec])
            return false;
        if (matrix[sr][sc] == target || target == matrix[er][ec] || target == matrix[sr][ec]
                || target == matrix[er][sc])
            return true;
        if (sr == er && sc == ec) {
            if (matrix[sr][sc] == target)
                return true;
            else
                return false;
        }
        int r1 = sr;
        // This is a little tricky. I am sure I will
        // Forget correct logic. But use copy pen and 
        // Then try to understand this logic.
        // For start row value check end column value
        while (matrix[r1][ec] < target) {
            r1++;
        }
        // For end row
        int r2 = er;
        while (matrix[r2][sc] > target) {
            r2--;
        }
        // For start col
        int c1 = sc;
        while (matrix[er][c1] < target) {
            c1++;
        }
        // For end col
        int c2 = ec;
        while (matrix[sr][c2] > target) {
            c2--;
        }
        showConfinedMatrix(matrix, target, r1, r2, c1, c2);
        return searchMatrix(matrix, target, r1, r2, c1, c2);
    }

    private void showConfinedMatrix(int[][] matrix, int target, int r1, int r2, int c1, int c2) {
        System.out.println("Printing matrix");
        for(int i=r1;i<=r2;i++){
            for(int j=c1;j<=c2;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }
    

}
