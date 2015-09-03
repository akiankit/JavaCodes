/*
X X X X
X O O X
X X O X
X O X X
After running your function, the board should be:

X X X X
X X X X
X X X X
X O X X
 */

package com.leetcode.medium;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class RowCol {
    int row;

    int col;

    public RowCol(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

public class SurroundedRegions {

    public static void main(String[] args) {
        char[][] board = {
                {
                        'X', 'X', 'X', 
                }, {
                        'X', 'O', 'X', 
                }, {
                        'X', 'X', 'X', 
                }
        };
        int r = board.length;
        int c = board[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        solve1(board);
        System.out.println();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void solve1(char[][] board) {
        int rown = board.length;
        if (rown==0) return;
        int coln = board[0].length;
        // 2 pass algorithm
        // In 1st pass whenever a 'O' is found at boundry then traverse for all 'O' which can be reached from this.
        // And change value to some thing which does not exist in array(say 1).
        // After this step all remaining 'O' are internal so they can be changed to 'X' as they are surrounded.
        // In 2nd pass change all O to X and all 1 to O
        for (int row=0; row<rown; ++row) {
            for (int col=0; col<coln; ++col) {
                if (row==0 || row==rown-1 || col==0 || col==coln-1) {
                    if (board[row][col]=='O') {
                        // An 'O' on boundry has been found so traverse for all
                        // Which can be reached from here.
                        Queue<Integer> q = new LinkedList<>();
                        board[row][col]='1';
                        q.add(row*coln+col);
                        // This loop is similar to what ever I have done in NumberOfIslands to
                        // traverse all '1' which can be reached from current 1.
                        while (!q.isEmpty()) {
                            // Instead of storing a number we can create a class of ROW_COL.
                            // That will be more clear to user for reading and understanding.
                            int cur = q.poll();
                            int x = cur/coln;
                            int y = cur%coln;
                            // Check for 'O' in possible 4 directions.
                            if (y+1<coln && board[x][y+1]=='O') {
                                q.add(cur+1);
                                board[x][y+1] = '1';
                            }
                            if (x+1<rown && board[x+1][y]=='O') {
                                q.add(cur+coln);
                                board[x+1][y] = '1';
                            }
                            if (y-1>=0 && board[x][y-1]=='O') {
                                q.add(cur-1);
                                board[x][y-1] = '1';
                            }
                            if (x-1>=0 && board[x-1][y]=='O') {
                                q.add(cur-coln);
                                board[x-1][y] = '1';
                            }
                        }
                    }
                }
            }
        }
        
        // 2nd pass
        for (int i=0; i<rown; ++i) {
            for (int j=0; j<coln; ++j) {
                if (board[i][j]=='O') {
                    board[i][j]='X';
                } else if (board[i][j]=='1') {
                    board[i][j]='O';
                }
            }
        }
    }

    public static void solve(char[][] board) {
        int r = board.length;
        if(r == 0)
            return;
        int c = board[0].length;
        boolean visited[][] = new boolean[r][c];
        for (int i = 1; i < r - 1; i++) {
            for (int j = 1; j < c - 1; j++) {
                if (board[i][j] == 'O' && visited[i][j] == false) {
                    checkAndMark(new RowCol(i, j), board, visited, r, c);
                }
                visited[i][j] = true;
            }
        }
    }
    
    private static boolean isSafe(int i, int j, char[][] board, boolean[][] visited) {
        if (i >= 0 && i < board.length && j >= 0 && j < board[0].length && board[i][j] == 'O'
                && visited[i][j] == false)
            return true;
        return false;
    }

    private static void checkAndMark(RowCol rowCol, char[][] board, boolean[][] visited, int row,
            int col) {
        boolean tempVisited[][] = new boolean[row][col];
        Stack<RowCol> stack = new Stack<RowCol>();
        Stack<RowCol> stackToMark = new Stack<RowCol>();
        stack.push(rowCol);
        boolean isPossible = true;
        while (!stack.isEmpty()) {
            RowCol temp = stack.pop();
            stackToMark.push(temp);
            int r = temp.row;
            int c = temp.col;
            tempVisited[r][c] = true;
            if ((r == row - 1 || r == 0 || c == col - 1 || c == 0) && board[r][c] == 'O') {
                isPossible = false;
                break;
            }
            if (isSafe(r + 1, c, board, tempVisited))
                stack.push(new RowCol(r + 1, c));
            if (isSafe(r, c + 1, board, tempVisited))
                stack.push(new RowCol(r, c + 1));
            if (isSafe(r - 1, c, board, tempVisited))
                stack.push(new RowCol(r - 1, c));
            if (isSafe(r, c - 1, board, visited))
                stack.push(new RowCol(r, c - 1));
        }
        if (isPossible == false) {
            while (stack.isEmpty() == false) {
                RowCol temp = stack.pop();
                visited[temp.row][temp.col] = true;
            }
            while (stackToMark.isEmpty() == false) {
                RowCol temp = stackToMark.pop();
                visited[temp.row][temp.col] = true;
            }
        } else {
            stack.push(rowCol);
            while (!stack.isEmpty()) {
                RowCol temp = stack.pop();
                int r = temp.row;
                int c = temp.col;
                visited[r][c] = true;
                board[r][c] = 'X';
                if (isSafe(r + 1, c, board, visited))
                    stack.push(new RowCol(r + 1, c));
                if (isSafe(r, c + 1, board, visited))
                    stack.push(new RowCol(r, c + 1));
                if (isSafe(r - 1, c, board, visited))
                    stack.push(new RowCol(r - 1, c));
                if (isSafe(r, c - 1, board, visited))
                    stack.push(new RowCol(r, c - 1));
            }
        }
    }

}
