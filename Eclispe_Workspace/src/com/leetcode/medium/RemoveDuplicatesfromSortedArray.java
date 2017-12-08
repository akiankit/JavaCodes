
package com.leetcode.medium;

public class RemoveDuplicatesfromSortedArray {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] A = {
                1, 1, 2, 2, 3
        };
        int duplicates = removeDuplicates(A);
        System.out.println(duplicates);
        for (int i = 0; i < duplicates; i++) {
            System.out.print(A[i] + " ");
        }
    }

    public static int removeDuplicates(int[] A) {
        int duplicateCount = 0;
        if(A == null)
            return 0;
        int n = A.length;
        if (n == 0 || n == 1)
            return 0;
        for(int i=1;i<n;i++){
            if(A[i] != A[i-1])A[i-duplicateCount] = A[i];
            else duplicateCount++;
        }
        return n - duplicateCount;
    }
}
