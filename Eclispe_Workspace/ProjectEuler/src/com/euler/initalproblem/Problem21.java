/*Let d(n) be defined as the sum of proper divisors of n (numbers less than n which divide evenly into n).
If d(a) = b and d(b) = a, where a ≠ b, then a and b are an amicable pair and each of a and b are called amicable numbers.

For example, 
the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284. 
The proper divisors of 284 are 1, 2, 4, 71 and 142; so d(284) = 220.

Evaluate the sum of all the amicable numbers under 10000.*/
package com.euler.initalproblem;

import java.util.LinkedList;
import java.util.List;

import util.NumberUtil;



public class Problem21 {
	
	private static final int limit = 10000;

	public static void main(String[] args) {
		long amicableSum = 0l;
		List<Integer> checkedNumber = new LinkedList<Integer>(); 
		for(int i=1;i<=limit;i++){
			if(checkedNumber.contains(i) == false){
				checkedNumber.add(i);
				List<Long> allFactors = NumberUtil.getProperDivisor(i);
				long sum = 0l;
				for (Long factor : allFactors) {
					sum +=factor;
				}
				if(sum > i &&  sum <= limit){
					checkedNumber.add((int) sum);
					allFactors = NumberUtil.getProperDivisor(sum);
					long sum2 = 0l;
					for (Long factor : allFactors) {
						sum2 +=factor;
					}
					if(sum2 == i){
						amicableSum += i+sum;
						//System.out.println("("+i+","+sum+")");
					}
				}
			}
		}
		System.out.println(amicableSum);
	}

}
