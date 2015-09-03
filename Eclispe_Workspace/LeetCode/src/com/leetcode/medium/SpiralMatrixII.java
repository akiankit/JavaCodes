package com.leetcode.medium;


public class SpiralMatrixII {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int num = 5;
        int[][] spiralOrder = spiralOrder(num);
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                System.out.print(spiralOrder[i][j] + ",");
            }
            System.out.println();
        }
    }

    public static int[][] spiralOrder(int num) {
        int[][] matrix = new int[num][num];
        int rowLength = num;
        int colLength = num;
        int numOfAddedElements = 0;
        int currentRowIndex = 0;
        int currentColIndex = 0;
        int topUnvisitedRow = 0;
        int bottomUnVisitedRow = rowLength - 1;
        int leftUnVisitedCol = 0;
        int rightUnVisitedCol = colLength - 1;
        boolean rowTraversalForward = true;
        boolean colTraversaldownward = false;
        boolean rowTraversalBackward = false;
        boolean colTraversalupward = false;
        int currentElement = 1;
        while (numOfAddedElements < rowLength * colLength && currentRowIndex < rowLength
                && currentColIndex < colLength) {
            matrix[currentRowIndex][currentColIndex] = currentElement++;
            if (rowTraversalForward) {
                if (currentColIndex == rightUnVisitedCol) {
                    topUnvisitedRow++;
                    currentRowIndex = currentRowIndex + 1;
                    rowTraversalForward = false;
                    colTraversaldownward = true;
                } else {
                    currentColIndex++;
                }
            } else if (colTraversaldownward) {
                if (currentRowIndex == bottomUnVisitedRow) {
                    rightUnVisitedCol--;
                    colTraversaldownward = false;
                    rowTraversalBackward = true;
                    currentColIndex = currentColIndex - 1;
                } else {
                    currentRowIndex++;
                }
            } else if (rowTraversalBackward) {
                if (currentColIndex == leftUnVisitedCol) {
                    bottomUnVisitedRow--;
                    rowTraversalBackward = false;
                    colTraversalupward = true;
                    currentRowIndex = currentRowIndex - 1;
                } else {
                    currentColIndex--;
                }
            } else if (colTraversalupward) {
                if (currentRowIndex == topUnvisitedRow) {
                    leftUnVisitedCol++;
                    colTraversalupward = false;
                    rowTraversalForward = true;
                    currentColIndex = currentColIndex + 1;
                } else {
                    currentRowIndex--;
                }
            }
            numOfAddedElements++;
        }
        return matrix;
    }

}
