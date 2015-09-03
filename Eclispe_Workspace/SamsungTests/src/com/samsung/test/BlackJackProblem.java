/*There will be N number of natural numbers given to you. Each represents a card number. 
 * Their range is from 1 to 13 and same number can appear multiple times. 
 * Your objective is to find the sum of the card numbers starting from first card, such that it should be maximized and it should not be exceeding 21.
 
For example, if the sequence is <5, 5, 6, 11>, then 5+5 = 10, 5+5+6 = 16, 5+5+6+11 = 27. 
So among these, maximized value which is less than or equal to 21 is 16. If the given sequence is <8, 2, 5> then sum of all is not exceed 21,
 to the maximum value will be 15.

Note that, 1, can be used either 1 or 11 (Ace in black jack). For example given sequence is <8, 1, 3> then possible sums can be 8, 9, 12 or 8, 19, 22. 
Among these possibilities, 19 is the maximized one. In actual black jack, 11, 12, 13 (J, Q, K) is considered as 10 but in this question 
please ignore this constraint and consider them as it is. But consider 1 as A (1 or 11).
 
N number of card will be given. Implement that maximized number which does not exceed 21 (Maximum value is 21,
 which is called black jack - if that comes only with 2 cards)
 

[Constraint]
Number of cards (N)  is  1≤N≤10.
Time limit: 1 sec with all 10 test cases
 

[Input]
10 test cases will be given. Each line is for each test case. First number will be N and then N number of cards will be given. All of the number is separated by space (‘ ’).
 

[Output]
Each line should contain maximum value. All lines start with ‘#x’, a space (‘ ‘) and value, where ‘x’ represents test case number.
 
[Input - output example] 
Input (max 10 lines)
4 5 5 6 11
3 8 2 5
3 8 1 3
…
 
Output (max 10 lines)
 

#1 16
#2 15
#3 19
…
 
 */
package com.samsung.test;

import java.util.Scanner;

public class BlackJackProblem {

	static int N;
	static int[] A = new int[10];
	static int Answer;

	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);

		for (int test_case = 1; test_case <= 10; test_case++) {
			N = sc.nextInt();

			for (int i = 0; i < N; i++) {
				A[i] = sc.nextInt();
			}

			// ///////////////////////////////////////////////////////////////////////////////////////////
			int finalIndex = 0;
			int tempSum = 0;
			for (int i = 0; i < N; i++) {
				finalIndex = i;
				tempSum = tempSum + A[i];
				if (tempSum > 21) {
					break;
				}
			}

			int sum[] = new int[2 * N];
			int temp = 0;
			sum[0] = A[0];
			if (A[0] == 1) {
				sum[1] = 11;
			}
			for (int i = 1; i < finalIndex; i++) {
				int j = 0;
				for (j = 0; j < sum.length && sum[j] != 0; j++) {
					int sum1 = sum[j] + A[i];
					if (sum1 < 22) {
						sum[j] = sum1;
					}
				}
				if (A[i] == 1) {
					int k = j;
					for (k = 0; k < j; k++) {
						int sum1 = sum[k] + 10;
						if (sum1 < 22) {
							sum[k + j] = sum1;
						}
					}
				}
			}
			Answer = sum[0];
			for (int i = 0; i < sum.length; i++) {
				if (Answer < sum[i]) {
					Answer = sum[i];
				}
			} // ///////////////////////////////////////////////////////////////////////////////////////////

			// standard output will be used for the evaluation.
			System.out.print("#" + test_case);
			System.out.println(" " + Answer);
		}
	}
}
