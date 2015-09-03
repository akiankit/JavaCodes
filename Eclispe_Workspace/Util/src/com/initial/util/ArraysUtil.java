
package com.initial.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ArraysUtil {
    
    public static void main(String[] args) {
        int[] num = {1, 2, 3, 4, 5, 6, 7};
        rotateArrayMethod3(num, 2);
        print1DArray(num);
    }

    public static void print1DArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                System.out.print(array[i] + ",");
            } else {
                System.out.println(array[i]);
            }
        }
    }

    public static void rotateArrayMethod3(int[] arr, int d){
        rotateArrayMethod3(arr, d, true);
    }

    // Reference http://www.geeksforgeeks.org/array-rotation/
    public static void rotateArrayMethod3(int[] arr, int d, boolean leftRotate) {
        if (!leftRotate) {
            d = arr.length - d;
        }
        int size = arr.length;
        for (int i = 0; i < gcd(size, d); i++) {
            int temp = arr[i];
            int k;
            int j = i;
            while (true) {
                k = j + d;
                if (k >= size)
                    k = k - size;
                if (k == i)
                    break;
                arr[j] = arr[k];
                j = k;
            }
            arr[j] = temp;
        }
    }

    public static int gcd(int a, int b) {
        if (b == 0)
            return a;
        else
            return gcd(b, a % b);
    }

    public static void getValidSubsetsForASum(int sum, int[] nums, int index,
            List<Integer> currSolution, Set<List<Integer>> solutionSets) {
        if (sum == 0) {
            solutionSets.add(new LinkedList<Integer>(currSolution));
        }
        if (sum < 0)
            return;
        if (index < nums.length) {
            getValidSubsetsForASum(sum, nums, index + 1, currSolution, solutionSets);
            int i = nums[index];
            currSolution.add(i);
            getValidSubsetsForASum(sum - i, nums, index + 1, currSolution, solutionSets);
            currSolution.remove(currSolution.get(currSolution.size() - 1));
        }
    }

    public static void getLengthNSubsets(int n, int[] nums, int index, List<Integer> currSolution,
            Set<List<Integer>> solutionSets) {
        if (n > nums.length)
            return;
        if (currSolution.size() > n)
            return;
        if (currSolution.size() == n) {
            solutionSets.add(new LinkedList<Integer>(currSolution));
        } else {
            if (index < nums.length) {
                getLengthNSubsets(n, nums, index + 1, currSolution, solutionSets);
                int i = nums[index];
                currSolution.add(i);
                getLengthNSubsets(n, nums, index + 1, currSolution, solutionSets);
                currSolution.remove(currSolution.get(currSolution.size() - 1));
            }
        }
    }

    public static void getLengthNSubsetsWithSumK(int k, int length, int[] nums, int index,
            List<Integer> currSolution, Set<List<Integer>> solutionSets) {
        Set<List<Integer>> solutionSets1 = new HashSet<List<Integer>>();
        getLengthNSubsets(length, nums, index, currSolution, solutionSets1);
        for (List<Integer> list : solutionSets1) {
            int sum = 0;
            for (Integer integer : list) {
                sum = sum + integer;
                if (sum > k) {
                    break;
                }
            }
            if (sum == k) {
                solutionSets.add(list);
            }
        }
    }

}
