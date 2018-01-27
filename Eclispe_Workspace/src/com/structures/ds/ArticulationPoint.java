package com.structures.ds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Idea is taken from this and some other places for other pdfs for
 * understanding. <br>
 * <a href=
 * "https://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/">Reference</a>
 * 
 * @author ankit
 *
 */
public class ArticulationPoint {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		int t = Integer.parseInt(line);
		for (int i = 0; i < t; i++) {
			line = sc.nextLine();
			String[] words = line.split(" ");
			int n = Integer.parseInt(words[0]);
			int e = Integer.parseInt(words[1]);
			Graph graph = new Graph(n, e);
			line = sc.nextLine();
			words = line.split(" ");
			for (int j = 0; j < words.length - 1; j += 2) {
				graph.addAdge(Integer.parseInt(words[j]), Integer.parseInt(words[j + 1]));
			}
			// graph.printGraph();
			graph.DFS();
			// graph.printParents();
			// graph.printDiscoveryTimes();
			// graph.printLowTimes();
			List<Integer> articulationPoints = graph.getArticulationPoints();
			// System.out.println(articulationPoints);
			System.out.println(articulationPoints.isEmpty() ? "1" : "0");
		}
		sc.close();
	}

	static class Graph {

		int numOfNodes;
		int numOfEdges;

		int[] nodes;
		int[][] adjNodes;

		/**
		 * For each node this value denotes the node from which current node is
		 * reached while doing DFS traversal.
		 */
		int[] parent;

		/**
		 * For each node this value denotes the minimum time in which it is
		 * possible to reach this node or any node in the subtree rooted at
		 * current node.
		 */
		int[] low;

		/**
		 * For each node this value denotes the time which is taken to reach the
		 * current node while doing DFS traversal
		 */
		int[] discoveryTime;

		boolean[] articulationPoint;
		boolean[] visitedNodes;

		int time = 0;

		public Graph(int numOfNodes, int numOfEdges) {
			this.numOfEdges = numOfEdges;
			this.numOfNodes = numOfNodes;
			nodes = new int[numOfNodes];
			adjNodes = new int[numOfNodes][numOfNodes];
			parent = new int[numOfNodes];
			for (int i = 0; i < parent.length; i++) {
				parent[i] = i;
			}
		}

		public void addAdge(int s, int d) {
			adjNodes[s][d] = 1;
			adjNodes[d][s] = 1;
		}

		public static void print2DArray(int[][] array) {
			for (int i = 0; i < array.length; i++) {
				System.out.println(Arrays.toString(array[i]));
			}
		}

		public int getChildCountForRoot() {
			int root = getRoot();
			int childCount = 0;
			for (int i = 0; i < numOfNodes; i++) {
				if (i != root && parent[i] == root)
					childCount++;
			}
			return childCount;
		}

		public int getRoot() {
			for (int i = 0; i < parent[i]; i++) {
				if (parent[i] == i)
					return i;
			}
			return 0;
		}

		public List<Integer> getArticulationPoints() {
			List<Integer> apPoints = new ArrayList<>(numOfNodes);
			int root = getRoot();
			articulationPoint[root] = false;
			int childCountForRoot = getChildCountForRoot();
			// System.out.println("childCountForRoot=" + childCountForRoot);
			if (childCountForRoot > 1) {
				articulationPoint[root] = true;
			}
			for (int i = 0; i < numOfNodes; i++) {
				if (articulationPoint[i] == true) {
					apPoints.add(i);
				}
			}
			return apPoints;
		}

		public void printGraph() {
			System.out.println();
			print2DArray(adjNodes);
		}

		public void printParents() {
			System.out.print("Parents:=>\t");
			System.out.println(Arrays.toString(parent));
		}

		public void printDiscoveryTimes() {
			System.out.print("discoveryTimes:=>\t");
			System.out.println(Arrays.toString(discoveryTime));
		}

		public void printLowTimes() {
			System.out.print("Low times:=>\t");
			System.out.println(Arrays.toString(low));
		}

		public void DFS() {
			visitedNodes = new boolean[numOfNodes];
			discoveryTime = new int[numOfNodes];
			articulationPoint = new boolean[numOfNodes];
			low = new int[numOfNodes];
			for (int i = 0; i < nodes.length; i++) {
				if (visitedNodes[i] == false) {
					DFS_Node(i);
				}
			}
		}

		private void DFS_Node(int node) {
			visitedNodes[node] = true;
			discoveryTime[node] = ++time;
			low[node] = discoveryTime[node];
			for (int i = 0; i < adjNodes.length; i++) {
				if (adjNodes[node][i] == 0) {
					continue;
				}
				if (visitedNodes[i] == false) {
					parent[i] = node;
					DFS_Node(i);
					/**
					 * Updates low values corresponding to tree edges
					 */
					low[node] = Math.min(low[i], low[node]);
					/**
					 * here we are making root node also as articulation point.
					 * Need to recalculate it again and set it correctly on the
					 * basis of child count
					 */
					if (low[i] >= discoveryTime[node]) {
						articulationPoint[node] = true;
					}
				}
				/**
				 * If it is parent that means it is a tree edge, we need to
				 * update the low time only corresponding to back edges.
				 */
				else if (i != parent[node]) {
					low[node] = Math.min(low[node], discoveryTime[i]);
				}

			}
		}
	}

}
