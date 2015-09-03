package com.samsung.test;

import java.util.Scanner;

/*
 As the name of the class should be Solution, using Solution.java as the filename is recommended.
 In any case, you can execute your program by running 'java Solution' command.
 */
class SudokuProblem {
	static int[][] A = new int[9][9];
	static int[] Answer = new int[15];
	static Boolean[] chec = new Boolean[9];

	static int K;

	public static void main(String args[]) throws Exception {
		/*
		 * The method below means that the program will read from input.txt,
		 * instead of standard(keyboard) input. To test your program, you may
		 * save input data in input.txt file, and call below method to read from
		 * the file when using nextInt() method. You may remove the comment
		 * symbols(//) in the below statement and use it. But before submission,
		 * you must remove the freopen function or rewrite comment symbols(//).
		 */
		// System.setIn(new FileInputStream("input.txt"));

		/*
		 * Make new scanner from standard input System.in, and read data.
		 */
		Scanner sc = new Scanner(System.in);

		/*
		 * Your program should handle 10 test cases given.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			 * Read each test case from standard input. Read the matrix with K
			 * modifications, and store it in A.
			 */
			K = sc.nextInt();
			int r = 0;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					A[i][j] = sc.nextInt();
				}
			}

			// ///////////////////////////////////////////////////////////////////////////////////////////
			/*
			 * Implement your algorithm here. The answer will be stored in array
			 * Answer. Total 3*K numbers should be stored, as each modification
			 * is printed as 3 numbers.
			 */
			int foundWrongs = 0;
			for (int i = 0; i < 9; i++) {
				if (foundWrongs == K) {
					break;
				}
				boolean isAnyNumberRepeated = false;
				boolean[] visited = new boolean[9];
				int repeatedNumber = 0;
				for (int j = 0; j < 9; j++) {
					int number = A[i][j];
					if (visited[number - 1] == false) {
						visited[number - 1] = true;
					} else {
						isAnyNumberRepeated = true;
						repeatedNumber = number;
					}
				}
				if (isAnyNumberRepeated == true) {
					int unvisitedNumber = 0;
					for (int j = 0; j < 9; j++) {
						if (visited[j] == false) {
							unvisitedNumber = j + 1;
							Answer[foundWrongs * 3] = i + 1;
							Answer[foundWrongs * 3 + 2] = unvisitedNumber;
						}
					}
					for (int j = 0; j < 9; j++) {
						if (repeatedNumber == A[i][j]) {
							int[] count = new int[9];
							for (int k = 0; k < 9; k++) {
								int number = A[k][j];
								if (count[number - 1] == 1 && number == repeatedNumber) {
									Answer[foundWrongs * 3 + 1] = j + 1;
									A[i][j] = unvisitedNumber;
									break;
								} else {
									count[number - 1] = 1;
								}
							}
						}
					}
					if (Answer[foundWrongs * 3 + 1] == 0) {
						for (int j = 0; j < 9; j++) {
							if (repeatedNumber == A[i][j]) {
								A[i][j] = unvisitedNumber;
								boolean[] tempVisited = new boolean[9];
								for (int k = 0; k < 9; k++) {
									int number = A[k][j];
									tempVisited[number - 1] = true;
								}
								boolean visitedEveryNode = true;
								for (int k = 0; k < 0; k++) {
									if (tempVisited[k] == false) {
										visitedEveryNode = false;
										break;
									}
								}
								if (visitedEveryNode == false) {
									A[i][j] = repeatedNumber;
								} else {
									Answer[foundWrongs * 3 + 1] = j + 1;
									break;
								}
							}
						}
					}
					foundWrongs++;
				}
			}

			// ///////////////////////////////////////////////////////////////////////////////////////////

			// Print the answer to standard output(screen).
			System.out.print("#" + test_case);
			for (int i = 0; i < 3 * K; i++) {
				System.out.print(" " + Answer[i]);
			}
			System.out.println();
		}
	}
}
