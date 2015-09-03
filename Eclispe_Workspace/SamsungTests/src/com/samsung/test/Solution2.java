package com.samsung.test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
   As the name of the class should be Solution, using Solution.java as the filename is recommended.
   In any case, you can execute your program by running 'java Solution' command.
 */
class Solution2
{
	static int K;
	static int[] A;
	static int Answer;
	
	static public int factorial(int n) {
		int factorial = 1;
		for (int i = 1; i <= n; i++) {
			factorial = factorial*i;
		}
		return factorial;
	}
	
	
	public static String getNthPermutation(int n, List<Integer> digits) {
		List<Integer> digits1 = new LinkedList<Integer>();
		digits1.addAll(digits);
		int location = n - 1;
		int digitsCount = digits1.size();
		StringBuilder number = new StringBuilder();
		for (int i = 0; i < digitsCount; i++) {
			int factorial = factorial(digitsCount - i - 1);
			int index = location%factorial;
			number.append(String.valueOf(digits1.get(index)));
			digits1.remove(index);
			location = location - index * factorial;
		}
		return number.toString();
	}

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

		for(int test_case = 1; test_case <= 2; test_case++)
		{
	   	/*
			 Read each test case from standard input.
		 */
			K = sc.nextInt();
			A = new int [4];
			List<Integer> digits = new LinkedList<Integer>();
			for(int i = 0; i < 4; i++)
			{
				A[i] = sc.nextInt();
				digits.add(A[i]);
			}


			/////////////////////////////////////////////////////////////////////////////////////////////
			Collections.sort(digits);
			String nthPermutation = getNthPermutation(K, digits);
			Answer = Integer.parseInt(nthPermutation);
			/////////////////////////////////////////////////////////////////////////////////////////////
			//Answer = -1;


			// Print the answer to standard output(screen).
			System.out.println("#" + test_case + " " + Answer);
		}
	}
}

