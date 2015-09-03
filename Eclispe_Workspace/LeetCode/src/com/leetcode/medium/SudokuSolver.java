package com.leetcode.medium;

import java.util.Arrays;

public class SudokuSolver {

    public static void main(String[] args) {
        char[][] board = {
                {
                        '5', '3', '.', '.', '7', '.', '.', '.', '.'
                }, {
                        '6', '.', '.', '1', '9', '5', '.', '.', '.'
                }, {
                        '.', '9', '8', '.', '.', '.', '.', '6', '.'
                }, {
                        '8', '.', '.', '.', '6', '.', '.', '.', '3'
                }, {
                        '4', '.', '.', '8', '.', '3', '.', '.', '1'
                }, {
                        '7', '.', '.', '.', '2', '.', '.', '.', '6'
                }, {
                        '.', '6', '.', '.', '.', '.', '2', '8', '.'
                }, {
                        '.', '.', '.', '4', '1', '9', '.', '.', '5'
                }, {
                        '.', '.', '.', '.', '8', '.', '.', '7', '9'
                }};
        solve(board);
        for(int i=0;i<board.length;i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
    
    public static boolean solve(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    for (char c = '1' ; c<='9' ; c++) {
                        if (isValid(board, i, j, c)) {
                            board[i][j] = c;
                            if (solve(board)) {
                                return true;
                            } else
                                board[i][j] = '.';
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isValid(char[][] board, int i, int j, char c) {
        // Check colum
        for (int row = 0; row < 9; row++)
            if (board[row][j] == c)
                return false;

        // Check row
        for (int col = 0; col < 9; col++)
            if (board[i][col] == c)
                return false;

        // Check 3 x 3 block
        for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++)
            for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++)
                if (board[row][col] == c)
                    return false;
        return true;
    }
    
}
