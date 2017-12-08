/*There are roads connecting cities in a shape of a graph. Below is an example. In here, vertices means cities, edges means roads (edges are undirected) 



Your objective is to make 2 grouping with a condition that cities in the same group are not directly connected each other.
For example, the above graph is separate in 2 groups as shown in below figure. Cities in the same group are not connected to each other directly. 
In this example, city 6 is not connected with any cities so it can be in either of the 2 groups.
﻿﻿﻿

N number of cities road map will be given. You have to identify the separation based on the above criteria. If not possible then, you need to prove it by printing -1. 
 

[constraint]
The number of vertices range is 5≤N≤1000.
Time limit: 1 sec with all sum of the 10 test cases
 

[input]
There will be 10 test cases, 2 lines for each test case. First line of the test case has number of vertices(N) and number of edges(E). Consecutive line will contain 2*E numbers. 
Each pair is an edge between two vertices. For example, the edge in between vertex 5 and vertex 28, will be expressed as “5 28�? or “28 5�?. Vertex number can be from 1 to N, 
every number is separated by one space(“ “).
 

[output]
There should be 10 lines of the output for the answer of each test case. Each line need to start with “#x�?(where x is test case number) and one space “ “ and the answer will be followed.
 Answer will be the number of the cities in the group (any one group) followed by the city numbers in that group. If it is not possible to differentiate with 2 groups, then print out -1.
 If there is only one possible group then print out 0. If there are many answers, you can put any one case.
 

[input - output example]
input (20 lines)

9 8 �? 1st case start
4 1 1 2 2 3 7 2 1 5 8 4 5 8 8 9
7 10 �? 2nd case start
1 2 2 3 1 6 4 6 2 4 2 7 2 5 6 7 3 5 5 7
...
 
 */
package com.samsung.test;

import java.util.Scanner;

public class CityGroupingProblem {

	static int V, E;
	static int[] E1, E2;
	static int AnswerN;
	static int[] Answer;

	public static void main(String args[]) throws Exception {

		/*
		 * data read from System.in
		 */
		Scanner sc = new Scanner(System.in);

		for (int test_case = 1; test_case <= 10; test_case++) {

			V = sc.nextInt();
			E = sc.nextInt();
			E1 = new int[E];
			E2 = new int[E];
			for (int i = 0; i < E; i++) {
				E1[i] = sc.nextInt();
				E2[i] = sc.nextInt();
			}

			// ///////////////////////////////////////////////////////////////////////////////////////////
			int group1[] = new int[V];
			int group2[] = new int[V];

			for (int i = 0; i < V; i++) {
				group1[i] = i + 1;
			}
			int k = 0;
			for (int i = 0; i < E; i++) {
				int firstVertex = E1[i];
				int secondVertex = E2[i];
				if (secondVertex < firstVertex) {
					int temp = firstVertex;
					firstVertex = secondVertex;
					secondVertex = temp;
				}
				int matchCount = 0;
				for (int j = 0; j < V; j++) {
					if (firstVertex == group1[j] || secondVertex == group1[j]) {
						matchCount++;
					}
				}
				if (matchCount == 2) {
					group2[k++] = secondVertex;
					int indexOfSecondVertex = 0;
					int j = 0;
					for (j = 0; group1[j] != secondVertex; j++)
						;
					indexOfSecondVertex = j;
					for (j = indexOfSecondVertex; j < V - 1; j++) {
						group1[j] = group1[j + 1];
					}
					group1[V - 1] = 0;
				}
			}
			int i = 0;
			for (i = 0; i < V && group1[i] != 0; i++)
				;
			int group1Length = i;
			for (i = 0; i < V && group2[i] != 0; i++)
				;
			int group2Length = i;
			if (group1Length == V) {
				AnswerN = 0;
			} else {
				boolean edgeExists = false;
				for (i = 0; i < group2Length; i++) {
					if (edgeExists == true) {
						break;
					}
					int firstVertex = group2[i];
					for (int j = i + 1; j < group2Length; j++) {
						if (edgeExists == true) {
							break;
						}
						int secondVertex = group2[j];
						for (k = 0; k < E; k++) {
							if ((E1[k] == firstVertex && E2[k] == secondVertex) || (E2[k] == firstVertex && E1[k] == secondVertex)) {
								AnswerN = -1;
								edgeExists = true;
								break;
							}
						}
					}
				}
			}
			if (AnswerN != -1) {
				AnswerN = group1Length;
				Answer = group1;
			}
			// ///////////////////////////////////////////////////////////////////////////////////////////
			// AnswerN = -1;
			// Answer = new int [V];

			// standard output will be used for the evaluation
			// if AnswerN is not -1, then print the list of the cities
			System.out.print("#" + test_case + " " + AnswerN);
			for (i = 0; i < AnswerN; i++) {
				System.out.print(" " + Answer[i]);
			}
			System.out.println();
		}
	}

}
