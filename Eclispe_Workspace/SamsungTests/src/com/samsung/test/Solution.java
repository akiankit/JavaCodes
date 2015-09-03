package com.samsung.test;

import java.util.Scanner;
import java.io.FileInputStream;

/*
   Use only Solution.java file and class name should be the same. Otherwise you will not get the score
*/

class Solution
{
	static int V, E;
	static int[] E1, E2;
	static int AnswerN;
	static int[] Answer;

	public static void main(String args[]) throws Exception
	{

		/*
		   data read from System.in
		*/
		Scanner sc = new Scanner(System.in);

		for(int test_case = 1; test_case <= 10; test_case++)
		{

			V = sc.nextInt();
			E = sc.nextInt();
			E1 = new int [E];
			E2 = new int [E];
			for(int i = 0; i < E; i++)
			{
				E1[i] = sc.nextInt();
				E2[i] = sc.nextInt();
			}


			/////////////////////////////////////////////////////////////////////////////////////////////
			int group1[] = new int[V];
			int group2[] = new int[V];
			
			for(int i=0;i<V;i++){
				group1[i] = i+1;
			}
			int k=0;
			for(int i=0;i<E;i++){
				int firstVertex = E1[i];
				int secondVertex = E2[i];
				if(secondVertex < firstVertex){
					int temp = firstVertex;
					firstVertex = secondVertex;
					secondVertex = temp;
				}
				int matchCount = 0;
				for(int j=0;j<V;j++){
					if(firstVertex == group1[j] || secondVertex == group1[j]){
						matchCount++;
					}
				}
				if(matchCount == 2){
					group2[k++] = secondVertex;
					int indexOfSecondVertex = 0;
					int j=0;
					for(j=0;group1[j]!=secondVertex;j++);
					indexOfSecondVertex = j;
					for(j=indexOfSecondVertex;j<V-1;j++){
						group1[j] = group1[j+1];
					}
					group1[V-1] = 0;
				}
			}
			int i=0;
			for(i=0;i<V && group1[i]!=0;i++);
			int group1Length = i;
			for(i=0;i<V && group2[i]!=0;i++);
			int group2Length = i;
			if (group1Length == V) {
				AnswerN = 0;
			}else {
				boolean edgeExists = false;
				for(i=0;i<group2Length;i++){
					if(edgeExists == true){
						break;
					}
					int firstVertex = group2[i];
					for(int j=i+1;j<group2Length;j++){
						if(edgeExists == true){
							break;
						}
						int secondVertex = group2[j];
						for(k=0;k<E;k++){
							if((E1[k] == firstVertex && E2[k] == secondVertex) || (E2[k] == firstVertex && E1[k] == secondVertex)){
								AnswerN=-1;
								edgeExists = true;
								break;
							}
						}
					}
				}
			}
			if(AnswerN !=-1){
				AnswerN = group1Length;
				Answer = group1;
			}  
			/////////////////////////////////////////////////////////////////////////////////////////////
			// AnswerN = -1;
			// Answer = new int [V];

			// standard output will be used for the evaluation
			// if AnswerN is not -1, then print the list of the cities
			System.out.print("#" + test_case + " " + AnswerN);
			for(i = 0; i < AnswerN; i++)
			{
				System.out.print(" " + Answer[i]);
			}
			System.out.println();
		}
	}
}

