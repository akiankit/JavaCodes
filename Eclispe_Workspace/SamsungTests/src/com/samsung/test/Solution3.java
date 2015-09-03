package com.samsung.test;
import java.util.Scanner;
import java.util.Stack;
/*
   As the name of the class should be Solution, using Solution.java as the filename is recommended.
   In any case, you can execute your program by running 'java Solution' command.
*/
class Solution3
{
	
	static int[][] Map = new int[501][501];
	static int Answer;
	static int N;
	
	static int count;

	public static void main(String args[]) throws Exception
	{
		/*
		   The method below means that the program will read from input.txt, instead of standard(keyboard) input.
   		   To test your program, you may save input data in input.txt file,
   		   and call below method to read from the file when using nextInt() method.
   		   You may remove the comment symbols(//) in the below statement and use it.
  		   But before submission, you must remove the freopen function or rewrite comment symbols(//).
   		 */

		// System.setIn(new FileInputStream("input.txt"));


		/*
		   Make new scanner from standard input System.in, and read data.
		*/
		Scanner sc = new Scanner(System.in);

		/*
		  Your program should handle 20 test cases given.
		 */
		for(int test_case = 1; test_case <= 20; test_case++)
		{
			/*
			   Read each test case from standard input.
			   A cell information of i-th row and j-th column is stored in Map[i][j].
			   (ex. If a number in the second row and the third column is one, one is stored in Map[2][3].)
			 */
			N = sc.nextInt();
			for (int i = 1; i <= N; i++) {
				for (int j = 1; j <= N; j++) {
					Map[i][j] = sc.nextInt();
				}
			}


			/////////////////////////////////////////////////////////////////////////////////////////////
			/*for(int i=1;i<501;i++){
				for(int j=1;j<501;j++){
					visited[i][j] = false;
				}
			}*/
			int clusterCounts = 0;
			for(int i=1;i<=N;i++){
				for(int j=1;j<=N;j++){
					if(Map[i][j] != -1){
						clusterCounts++;
						visitAllPossible(Map[i][j], Map, i, j, N+1);
						/*for(int k=1;k<=N;k++){
							for(int l=1;l<=N;l++){
								if(l!=N){
									System.out.print(Map[k][l]+" ");
								}else{
									System.out.println(Map[k][l]);
								}
							}
						}*/
					}
				}
			}
			Answer = clusterCounts;
			/////////////////////////////////////////////////////////////////////////////////////////////
			//Answer = -1;


			// Print the answer to standard output(screen).
			System.out.println("#" + test_case + " " + Answer);
		}
	}

	private static void visitAllPossible(int number, int[][] map2, int row, int col, int SIZE) {
		Stack<Integer> rowPosition = new Stack<Integer>();
		Stack<Integer> colPosition = new Stack<Integer>();
		rowPosition.push(row);
		colPosition.push(col);
		while(rowPosition.isEmpty() == false && colPosition.isEmpty() == false){
			row = rowPosition.pop();
			col = colPosition.pop();
			map2[row][col] = -1;
			if (col < SIZE - 1 && map2[row][col + 1] == number ) {
				rowPosition.push(row);
				colPosition.push(col+1);
			}
			if (col != 1 && map2[row][col - 1] == number ) {
				rowPosition.push(row);
				colPosition.push(col-1);
			}
			if (row < SIZE - 1 && map2[row + 1][col] == number ) {
				rowPosition.push(row+1);
				colPosition.push(col);
			}
			if (row != 1 && map2[row - 1][col] == number ) {
				rowPosition.push(row-1);
				colPosition.push(col);
			}
		}
 				
	}
	
	public static void printArray(int[] array){
		for(int i=0;i<array.length;i++){
			if(i!=array.length-1){
				System.out.print(array[i]+" ");
			}else{
				System.out.println(array[i]);
			}
		}
	}
	
	
	/*private static void visitAllPossible(int number, int[][] box, int row, int col,int SIZE) {
		visited[row][col] = true;
		if (col != SIZE - 1 && box[row][col + 1] == number && visited[row][col+1] == false) {
			visitAllPossible(number, box, row, col + 1, SIZE);
		}
		if (col != 1 && box[row][col - 1] == number && visited[row][col-1] == false) {
			visitAllPossible(number, box, row, col - 1, SIZE);
		}
		if (row != SIZE - 1 && box[row + 1][col] == number && visited[row+1][col] == false) {
			visitAllPossible(number, box, row + 1, col, SIZE);
		}
		if (row != 1 && box[row - 1][col] == number && visited[row-1][col] == false) {
			visitAllPossible(number, box, row - 1, col, SIZE);
		}
	}*/
}

