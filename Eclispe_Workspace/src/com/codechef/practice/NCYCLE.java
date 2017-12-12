/*
 We can "walk around" a permutation in a interesting way and here
is how it is done for the permutation above:
Start at position 1. At position 1 we have 2 and so we go to
position 2. Here we find 4 and so we go to position 4. Here we find
1, which is a position that we have already visited. This completes
the first part of our walk and we denote this walk by (1 2 4 1). Such
a walk is called a cycle. An interesting property of such
walks, that you may take for granted, is that the position we revisit
will always be the one we started from!
We continue our walk by jumping to first unvisited position, in
this case position 3 and continue in the same manner. This time we
find 5 at position 3 and so we go to position 5 and find 7 and we go
to position 7 and find 3 and thus we get the cycle (3 5 7 3). Next we
start at position 6 and get (6 6) and finally we start at position 8
and get the cycle (8 8). We have exhausted all the positions. Our
walk through this permutation consists of 4 cycles.
One can carry out this walk through any permutation and obtain a
set of cycles as the result. Your task is to print out the cycles
that result from walking through a given permutation.
Input format
The first line of the input is a positive integer N
indicating the length of the permutation. The next line contains
N integers and is a permutation of 1,2,...,N.
You may assume that N ≤ 1000.
Output format
The first line of the output must contain a single integer
k denoting the number of cycles in the permutation. Line 2
should describe the first cycle, line 3 the second cycle and so on and
line k+1 should describe the kth cycle.
Sample input 1:
8
2 4 5 1 7 6 3 8
Sample output 1:
4
1 2 4 1
3 5 7 3
6 6
8 8 
 */
package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class NCYCLE {/*

	public static void main(String[] args) {
		System.out.println("hello world");
		Scanner scanner = new Scanner(System.in);
		int totalLength = scanner.nextInt();
		int input[] = new int[totalLength];
		List<Integer> inputList = new LinkedList<Integer>();
		for (int i = 0; i < totalLength; i++) {
			int nextInt = scanner.nextInt();
			input[i] = nextInt;
			inputList.add(nextInt);
		}
		scanner.close();
		List<List<Integer>> cycles = new LinkedList<List<Integer>>();
		while (inputList.size() > 0) {
			int unvisitedNumber = firstUnvisitedNumber(inputList);
			List<Integer> tempCycle = new LinkedList<Integer>();
			tempCycle.add(unvisitedNumber);
			int nextNum = input[unvisitedNumber-1];
			while(nextNum != unvisitedNumber){
				tempCycle.add(nextNum);
				inputList.remove(inputList.indexOf(nextNum));
				nextNum = input[nextNum-1];
			}
			tempCycle.add(nextNum);
			inputList.remove(inputList.indexOf(nextNum));
			cycles.add(tempCycle);
		}
		System.out.println(cycles.size());
		for (List<Integer> list : cycles) {
			for (Integer integer : list) {
				System.out.print(integer +" ");
			}
			System.out.println();
		}
	}

	private static int firstUnvisitedNumber(List<Integer> inputList) {
		int minimum = inputList.get(0);
		for (Integer integer : inputList) {
			if (integer < minimum) {
				minimum = integer;
			}
		}
		return minimum;
	}

*/}