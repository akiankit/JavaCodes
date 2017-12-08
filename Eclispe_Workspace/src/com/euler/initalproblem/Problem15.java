/*Starting in the top left corner of a 2�2 grid, and only being able to move to the right and down,
there are exactly 6 routes to the bottom right corner.


How many such routes are there through a 20�20 grid?*/
package com.euler.initalproblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Problem15 {

	private static int length = 3;

	private static int maxIndex = length;
	
	private static long[][] pathCounts = new long[length+1][length+1];

	private static long count = 0;
	
	private static long mod = 1000000007;
	
	static {
	    for(int i=0;i<length;i++){
            pathCounts[i][length] = 1;
            pathCounts[length][i] = 1;
        }
	    /*for(int i=1;i<length;i++){
	        for(int j=1;j<length;j++){
	            pathCounts[i][j] = pathCounts[i-1][j]+pathCounts[i][j-1];
	        }
	    }*/
        for (int i = length - 1; i >= 0; i--) {
            for (int j = length - 1; j >= 0; j--) {
                pathCounts[i][j] = (pathCounts[i][j + 1] + pathCounts[i + 1][j]) % mod;
            }
        }
	}
	
	private static void print2Darray(long[][] adjacenyMatrix) {
        System.out.println();
        for(int i=0;i<adjacenyMatrix.length;i++){
            System.out.println(Arrays.toString(adjacenyMatrix[i]));
        }
    }

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int tests = scanner.nextInt();
		for(int i=0;i<tests;i++) {
		    int a = scanner.nextInt();
		    int b = scanner.nextInt();
		    System.out.println(pathCounts[length-a][length-b]);
		}
		scanner.close();
	}

	//Dynamic Programming Approach
	private static long getTotalPathCount(int iStart,int jStart){
		for(int i=0;i<length;i++){
			pathCounts[i][length] = 1;
			pathCounts[length][i] = 1;
		}
		for(int i=length-1;i>=0;i--){
			for(int j=length-1;j>=0;j--){
				pathCounts[i][j] = pathCounts[i][j+1] + pathCounts[i+1][j];
			}
		}
		return pathCounts[iStart][jStart];
	}
	
	private static int getPathCount() {
		boolean end = false;
		List<Long[]> list = new ArrayList<Long[]>();
		list.add(new Long[]{0l,0l});
		while(false == end){
			List<Long[]> tempList = new ArrayList<Long[]>();
			for (Long[] longs : list) {
				long row = longs[0];
				long column = longs[1];
				if (maxIndex == row && column < maxIndex) {
					tempList.add(new Long[]{row,column+1});
				} else if (maxIndex == column && row < maxIndex) {
					tempList.add(new Long[]{row+1,column});
				} else if (row < maxIndex && column < maxIndex) {
					tempList.add(new Long[]{row+1,column});
					tempList.add(new Long[]{row,column+1});
				}
			}
			for (Long[] longs : tempList) {
				long row = longs[0];
				long column = longs[1];
				if(row==maxIndex && column == maxIndex){
					end = true;
				}else {
					end =  false;
					break;
				}
			}
			list.clear();
			list = tempList;
		}
		return list.size();
	}

	/*private static void getPathCount(int row, int column) {
		if (maxIndex == row && maxIndex == column) {
			count++;
		} else {
			if (maxIndex == row && column < maxIndex) {
				getPathCount(row, column + 1);
			} else if (maxIndex == column && row < maxIndex) {
				getPathCount(row + 1, column);
			} else if (row < maxIndex && column < maxIndex) {
				getPathCount(row, column + 1);
				getPathCount(row + 1, column);
			}

		}
		return;
	}*/
}
