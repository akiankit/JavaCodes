package com.leetcode.arrays;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CombinationSum {

    
    static List<List<Integer>> finalList = new LinkedList<List<Integer>>();
    static List<List<Integer>> finalList1 = new LinkedList<List<Integer>>();
    static int sum = 0;
    static int[] arr;

    public static void main(String[] args) {
        int[] coins = {10,1,2,7,6,1,5};
        combinationSum(coins, 8);
        System.out.println(finalList);
        combinationSum1(coins, 8);
        System.out.println(finalList1);
    }

    static public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        arr = candidates;
        sum = target;
        combinationSumUtil(0, 0, new LinkedList<Integer>());
        return finalList;
    }
    
    static public List<List<Integer>> combinationSum1(int[] candidates, int target) {
        Arrays.sort(candidates);
        arr = candidates;
        sum = target;
//        for(int i=0;i<arr.length;i++)
        combinationSumUtil1(-1, 0, new LinkedList<Integer>());
        return finalList1;
    }
    
    private static boolean combinationSumUtil(int index, int currentSum, List<Integer> list) {
        if(currentSum > sum) {
            return false;
        }
        if(currentSum == sum) {
            if(!finalList.contains(list))
                finalList.add(new LinkedList<Integer>(list));
            return true;
        }
        for(int i=index; i<arr.length; i++){
            list.add(arr[i]);
            combinationSumUtil(i, currentSum + arr[i], list);
            list.remove(list.size()-1); 
        }
        return false;
    }
    
    private static boolean combinationSumUtil1(int index, int currentSum, List<Integer> list) {
        if(currentSum > sum) {
            return false;
        }
        if(currentSum == sum) {
            if(!finalList1.contains(list))
                finalList1.add(new LinkedList<Integer>(list));
            return true;
        }
        for(int i=index+1; i<arr.length; i++) {
            list.add(arr[i]);
            combinationSumUtil1(i, currentSum + arr[i], list);
            list.remove(list.size()-1); 
        }
        return false;
    }
}
