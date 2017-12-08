 
/*1st Step-Try to find out if two vertex Vertex A and B are connected in some manner.
2nd Step -Print the path from Vertex A to Vertex B.
3rd Step- Print all Paths possible from Vertex A to vertex B.*/

package com.test.sep3;

import java.util.Scanner;

public class Solution3 {

	/*
	 * As the name of the class should be Solution, using Solution.java as the
	 * filename is recommended. In any case, you can execute your program by
	 * running 'java Solution' command.
	 */
	static int V, E;
	static int[] E1, E2;
	static int Answer1;
	static int[] Answer2;

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
		Answer2 = new int[3];

		/*
		 * Your program should handle 15 test cases given.
		 */
		for (int test_case = 1; test_case <= 1; test_case++) {
			/*
			 * Read each test case from standard input. The number of cities and
			 * edges are stored in V and E, respectively. Cities connected by
			 * i-th edge is E1[i] and E2[i].
			 */
			V = sc.nextInt();
			E = sc.nextInt();
			E1 = new int[E];
			E2 = new int[E];
			for (int i = 0; i < E; i++) {
				E1[i] = sc.nextInt();
				E2[i] = sc.nextInt();
			}
			sc.close();
			// ///////////////////////////////////////////////////////////////////////////////////////////
			int[][] connections = new int[V][V - 1];
			int[] edgePerVertex = new int[V];
			for (int i = 0; i < E; i++) {
				int index1 = E1[i] - 1;
				int index2 = E2[i] - 1;
				connections[index1][edgePerVertex[index1]] = E2[i];
				edgePerVertex[index1] = edgePerVertex[index1] + 1;
				connections[index2][edgePerVertex[index2]] = E1[i];
				edgePerVertex[index2] = edgePerVertex[index2] + 1;
			}
			int maxEdges = edgePerVertex[0];
			int maxEdgesIndex = 0;
			for (int i = 0; i < V; i++) {
				if (maxEdges < edgePerVertex[i]) {
					maxEdges = edgePerVertex[i];
					maxEdgesIndex = i;
				}
			}
			Answer1 = 1;
			Answer2[0] = maxEdgesIndex + 1;
			
			for (int i = 0; i < V; i++) {
				System.out.print((i + 1) + "-->");
				for (int j = 0; j < edgePerVertex[i]; j++) {
					System.out.print(connections[i][j] + ",");
				}
				System.out.println();
			}
			 
			int vertexA = 2;
			int vertexB = 4;
			int removeA = 1;
			int removeB = 5;
			boolean connected = areVertexConnected(connections, vertexA, vertexB, V, removeA, removeB,vertexA);
			if(connected == false){
				System.out.println("VertexA="+vertexA+" vertexB="+vertexB+" are not connected after removing "+removeA+" "+removeB);
			}else{
				System.out.println("VertexA="+vertexA+" vertexB="+vertexB+" are connected after removing "+removeA+" "+removeB);
			}
			
			
			// ///////////////////////////////////////////////////////////////////////////////////////////
			// Answer1 = 0;

			// Print the answer to standard output(screen).
			/*System.out.print("#" + test_case + " " + Answer1);
			for (int i = 0; i < Answer1; i++)
				System.out.print(" " + Answer2[i]);
			System.out.println();*/
		}
	}
	
	static boolean areVertexConnected(int[][] connectivity,int vertexA,int vertexB,int V,int removeA,int removeB,int tempVertexA){
		if(vertexA == removeA || vertexB == removeB || vertexA == removeB || vertexB == removeA){
			return false;
		}
		int[] connectivityA = connectivity[tempVertexA-1];
		if(contains(connectivityA,vertexB) == true){
			System.out.println(tempVertexA+"->"+vertexB);
			return true;
		}else{
			for(int i=0;i<connectivityA.length;i++){
				int tempVertexA1 = connectivityA[i];
				if(tempVertexA1 == removeA || tempVertexA1 == removeB){
					continue;
				}
				if(tempVertexA1 == vertexA || tempVertexA1 ==0){
					continue;
				}
				if(areVertexConnected(connectivity, vertexA, vertexB, V, removeA, removeB,tempVertexA1) == true){
					System.out.println(vertexA+"-->");
					return true;
				}
			}
		}
		return false;
	}

	private static boolean contains(int[] connectivityA, int vertexB) {
		for(int i=0;i<connectivityA.length;i++){
			if(connectivityA[i] == vertexB){
				return true;
			}
		}
		return false;
	}

}
