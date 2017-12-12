package com.hackerearth.practive;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Monk_and_Tree {

	static class Edge {

		public int node1;
		public int node2;
		public int weight;

		public Edge(int node1, int node2) {
			if (node1 > node2) {
				int node3 = node1;
				node1 = node2;
				node2 = node3;
			}
			this.node1 = node1;
			this.node2 = node2;
			this.weight = Math.abs(node1 - node2);
		}

		@Override
		public String toString() {
			return "(" + node1 + "," + node2 + ")[" + weight + "]";
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int j = 0; j < testCases; j++) {
			int n = scanner.nextInt();
			int[] nodes = new int[n + 1];
			int[] parent = new int[n + 1];
			for (int i = 0; i <= n; i++) {
				parent[i] = i;
			}
			for (int i = 0; i <= n; i++) {
				nodes[i] = i;
			}
			int m = scanner.nextInt();
			Edge[] edges = new Edge[m];
			for (int i = 0; i < m; i++) {
				int node1 = scanner.nextInt();
				int node2 = scanner.nextInt();
				edges[i] = new Edge(node1, node2);
			}
			Arrays.sort(edges, new Comparator<Edge>() {

				@Override
				public int compare(Edge o1, Edge o2) {
					return o2.weight - o1.weight;
				}
			});
			System.out.println(Arrays.toString(edges));
			int cost = 0;
			DisjointSetUsingArray disjointSet = new DisjointSetUsingArray(n);
			for (int i = 1; i <= n; i++) {
				disjointSet.makeSet(i);
			}
			System.out.println(disjointSet);
			for (int i = 0; i < edges.length; i++) {
				int u = edges[i].node1;
				int v = edges[i].node2;
				if (disjointSet.find(u) != disjointSet.find(v)) {
					disjointSet.union(u, v);
				} else {
					cost += edges[i].weight;
				}
				System.out.println(disjointSet);
			}
			System.out.println(disjointSet);
			Set<Integer> components = new HashSet<>();
			int[] parents = disjointSet.getParents();
			for (int i = 1; i <= n; i++) {
				components.add(parents[i]);
			}
			cost += components.size() - 1;
			System.out.println(cost);
		}
		scanner.close();
	}

	public static int findSet(int u, int[] parent) {
		if (u != parent[u]) {
			return findSet(parent[u], parent);
		}
		return u;
	}

	public static void unionSet(int u, int v, int[] parent) {
		int findSetU = findSet(u, parent);
		int findSetV = findSet(v, parent);
		if (findSetU == findSetV) {
			return;
		}
		parent[v] = parent[u];
	}

}

class DisjointSetUsingArray {

	private int[] parents;

	private int[] rank;

	public DisjointSetUsingArray(int size) {
		setParents(new int[size + 1]);
		rank = new int[size + 1];
	}

	public void makeSet(int x) {
		rank[x] = 0;
		parents[x] = x;
	}

	public int find(int x) {
		if (parents[x] == x)
			return x;
		return find(parents[x]);
	}

	public void union(int x, int y) {
		int xRep = find(x);
		int yRep = find(y);
		if (xRep == yRep)
			return;
		if (rank[xRep] < rank[yRep]) {
			parents[xRep] = yRep;
			rank[xRep]++;
		} else if (rank[yRep] < rank[xRep]) {
			parents[yRep] = xRep;
			rank[yRep]++;
		} else {
			parents[yRep] = xRep;
			rank[xRep] += rank[yRep];
		}
	}

	@Override
	public String toString() {
		return Arrays.toString(getParents()) + " " + Arrays.toString(rank);
	}

	/**
	 * @return the parents
	 */
	public int[] getParents() {
		return parents;
	}

	/**
	 * @param parents
	 *            the parents to set
	 */
	public void setParents(int[] parents) {
		this.parents = parents;
	}

}
