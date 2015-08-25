package com.leetcode.easy;

import java.util.*;

public class SummaryRanges {

	public static void main(String[] args) {
		int[] arr = {0,2,4,5,7};
		System.out.println(summaryRanges(arr));
	}

    public static List<String> summaryRanges(int[] nums) {
        List<String> res = new LinkedList<String>();
        int start = -1;
        for(int i=0;i<nums.length;i++){
        	StringBuilder sb = new StringBuilder();
        	if(start == -1){
        		start = nums[i];
        		int count =1;
        		while(i+1<nums.length && nums[i+1]-nums[i] == 1){
        			count++;
        			i++;
        		}
        		if(count >1){
        			sb.append(start).append("->").append(nums[i]);
        			start = -1;        			
        		} else{
        			sb.append(start);
        			start = -1;
        		}
        		res.add(sb.toString());
        	}
        	sb = new StringBuilder();
        }
        return res;
    }
}
