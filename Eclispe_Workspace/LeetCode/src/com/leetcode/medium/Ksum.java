package com.leetcode.medium;

public class Ksum {
    /**
     * @param A: an integer array.
     * @param k: a positive integer (k <= length(A))
     * @param target: a integer
     * @return an integer
     */
    public int kSum(int A[], int k, int target) {
        return kSumUtil(A,k,target,0,0);
    }

    
    public static void main(String[] args) {
		Ksum ksum = new Ksum();
		int[] A = {1,3,4,5,8,10,11,12,14,17,20,22,24,25,28,30,31,34,35,37,38,40,42,44,45,48,51,54,56,59,60,61,63,66};
		int k = 4;
		int target = 13;
		int ans = ksum.kSum(A, k, target);
		System.out.println(ans);
	}

    private int kSumUtil(int[] A, int k, int target, int index, int currentCount) {
//    	System.out.println("Target ="+target+" currentCount="+currentCount +" index="+index);
        int count = 0;
        if(target == 0) {
            if(currentCount == k) {
                return 1;
            }
            return 0;
        }
        if(target <0) {
            return 0;
        }
        if(index >=A.length)
            return 0;
        if(A[index] > target) {
        	count += kSumUtil(A, k, target, index+1, currentCount);
        	return count;
        }
        count += kSumUtil(A, k, target, index+1, currentCount);
        count += kSumUtil(A, k, target-A[index], index+1, currentCount+1);
        return count;
    }
}

