
package com.structures.ds;


public class DisjointSetTest {
    public static void main(String[] args) {
        int[] nums = {
                0, 1, 2, 3, 4, 5, 6, 7
        };
        DisjointSetUsingArray disjointSet = new DisjointSetUsingArray(nums.length);
        for (int i = 0; i < nums.length; i++) {
            disjointSet.makeSet(nums[i]);
        }
        System.out.println(disjointSet);
        disjointSet.union(0, 4);
        System.out.println(disjointSet);
//        disjointSet.union(0, 3);
//        System.out.println(disjointSet);
        disjointSet.union(3, 5);
        System.out.println(disjointSet);
    }
}

/*class DisjointSetUsingArray {

    private int[] parents;

    private int[] rank;

    public DisjointSetUsingArray(int size) {
        parents = new int[size + 1];
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
        } else if (rank[yRep] < rank[xRep]) {
            parents[yRep] = xRep;
        }else {
            parents[yRep] = xRep;
            rank[xRep]++;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(parents) + " " + Arrays.toString(rank);
    }

}
*/