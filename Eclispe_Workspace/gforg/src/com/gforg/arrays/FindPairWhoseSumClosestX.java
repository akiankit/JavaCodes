/*Given two sorted arrays and a number x, find the pair whose sum is closest to x and the pair has an element from each array.

We are given two arrays ar1[0...m-1] and ar2[0..n-1] and a number x,
we need to find the pair ar1[i] + ar2[j] such that absolute value of (ar1[i] + ar2[j] – x) is minimum.

Example:

Input:  ar1[] = {1, 4, 5, 7};
        ar2[] = {10, 20, 30, 40};
        x = 32
Output:  1 and 30

Input:  ar1[] = {1, 4, 5, 7};
        ar2[] = {10, 20, 30, 40};
        x = 50
Output:  7 and 40*/
package com.gforg.arrays;

public class FindPairWhoseSumClosestX {

    public static void main(String[] args) {

    }

    public static int[] findPair(int[] A, int[] B, int target) {
        int[] indexes = new int[2];
        int aIndex = 0;
        int bIndex = 0;
        indexes[0] = aIndex;
        indexes[1] = bIndex;
        for (int i = 0, j = 0; i < A.length && j < B.length;) {

        }
        return indexes;
    }
}
