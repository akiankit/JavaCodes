package com.hackerrank.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class MergingCommunities {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numsCount = scanner.nextInt();
        int count = scanner.nextInt();
        DisjointSetUsingArray disjointSet = new DisjointSetUsingArray(numsCount);
        for (int i = 1; i <= numsCount; i++) {
            disjointSet.makeSet(i);
        }
        for (int i = 0; i < count; i++) {
            String next = scanner.next();
            if (next.equalsIgnoreCase("Q")) {
                int a = scanner.nextInt();
                System.out.println(disjointSet.getSize(a));
            } else {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                disjointSet.union(a, b);
            }
        }
        scanner.close();
    }

}

class DisjointSetUsingArray {

    private int[] parents;

    private int[] rank;
    
    private int[] size;
    
    public int getSize(int x) {
        return size[find(x)];
    }

    public DisjointSetUsingArray(int count) {
        parents = new int[count + 1];
        rank = new int[count + 1];
        size = new int[count + 1];
    }

    public void makeSet(int x) {
        rank[x] = 0;
        parents[x] = x;
        size[x] = 1;
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
            size[yRep] += size[xRep];
        } else if (rank[yRep] < rank[xRep]) {
            parents[yRep] = xRep;
            size[xRep] += size[yRep];
        } else {
            parents[yRep] = xRep;
            rank[xRep]++;
            size[xRep] += size[yRep];
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(parents) + " " + Arrays.toString(rank);
    }

}
