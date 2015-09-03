package com.categorize.late;

//http://sudhansu-codezone.blogspot.in/2012/03/maximum-possible-sum-of-non-consecutive.html
public class MaxSumNonConsecutive {
	static int[] input = {6, 4, 2, 8, 1};
	
    public static void main(String[] args) {
        int[] maxSum = new int[input.length];
        maxSum[0] = input[0];
        maxSum[1] = Math.max(input[0], input[1]);
        for (int i = 2; i < input.length; i++) {
            maxSum[i] = Math.max(maxSum[i - 2] + input[i], maxSum[i - 1]);
        }
        for (int i = 0; i < input.length; i++) {
            System.out.println("Till index=" + i + " Max Sum=" + maxSum[i]);
        }
    }

}
