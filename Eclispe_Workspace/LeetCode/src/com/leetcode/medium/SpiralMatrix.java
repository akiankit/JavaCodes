/*Given a matrix of m x n elements (m rows, n columns), return all elements of the matrix in spiral order.

For example,
Given the following matrix:

[
 [ 1, 2, 3 ],
 [ 4, 5, 6 ],
 [ 7, 8, 9 ]
]

You should return [1,2,3,6,9,8,7,4,5].*/

package com.leetcode.medium;

import java.util.LinkedList;
import java.util.List;

public class SpiralMatrix {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // int[][] matrix = {{ 1, 2, 3 },{ 4, 5, 6 },{ 7, 8, 9 }};
        //int[][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
        int[][] matrix = {
                {
                        1, 2, 3, 4, 5
                }, {
                        6, 7, 8, 9, 10
                }, {
                        11, 12, 13, 14, 15
                }, {
                        16, 17, 18, 19, 20
                }, {
                        21, 22, 23, 24, 25
                }
        };
        //int[][] matrix = {{3},{2}};
        System.out.println(spiralOrder(matrix));
    }

    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> spiralList = new LinkedList<Integer>();
        int rowLength = matrix.length;
        int colLength = 0;
        if (rowLength > 0) {
            colLength = matrix[0].length;
        }
        int numOfVisitedElements = 0;
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
        while (numOfVisitedElements < rowLength * colLength && currentRowIndex < rowLength
                && currentColIndex < colLength) {
            spiralList.add(matrix[currentRowIndex][currentColIndex]);
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
            numOfVisitedElements++;
        }
        return spiralList;
    }

}
