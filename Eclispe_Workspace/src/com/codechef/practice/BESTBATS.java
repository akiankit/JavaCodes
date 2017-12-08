package com.codechef.practice;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BESTBATS {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		int playersCount = 11;
		int scores[] = new int[playersCount];
		int scores2[] = new int[playersCount];
		for (int i = 0; i < testCases; i++) {
			for(int j=0;j<playersCount;j++){
				int score = scanner.nextInt();
				scores2[j] = scores[j] = score;
			}
			Arrays.sort(scores);
			int groupLength = scanner.nextInt();
			int sum = 0;
			for(int j=playersCount-groupLength;j<playersCount;j++){
				sum = sum +scores[j];
			}
			List<Integer> currSolution = new LinkedList<Integer>();
			List<List<Integer>> solutionSets = new LinkedList<List<Integer>>();
			getLengthNSubsetsWithSumK(sum, groupLength, scores, 0, currSolution, solutionSets);
			System.out.println(solutionSets.size());
		}
		scanner.close();
	}

	public static void getLengthNSubsets(int n,int[] nums,int index,List<Integer> currSolution,List<List<Integer>> solutionSets){
		if(n > nums.length){
			return;
		}
		if(currSolution.size() > n){
			return;
		}
		if(currSolution.size() == n){
			solutionSets.add(new LinkedList(currSolution));
		}else{
			if(index<nums.length){
				getLengthNSubsets(n,nums,index+1,currSolution,solutionSets);
				int i = nums[index];
				currSolution.add(i);
				getLengthNSubsets(n, nums, index+1, currSolution,solutionSets);
				currSolution.remove(currSolution.get(currSolution.size()-1));
			}
		}
	}
	
	public static void getLengthNSubsetsWithSumK(int k,int length,int[] nums,int index,List<Integer> currSolution,List<List<Integer>> solutionSets){
		List<List<Integer>> solutionSets1 = new LinkedList<List<Integer>>();
		getLengthNSubsets(length, nums, index, currSolution, solutionSets1);
		for (List<Integer> list : solutionSets1) {
			int sum = 0;
			for (Integer integer : list) {
				sum = sum+ integer;
				if(sum > k){
					break;
				}
			}
			if(sum==k){
				solutionSets.add(list);
			}
		}
	}
	
}
