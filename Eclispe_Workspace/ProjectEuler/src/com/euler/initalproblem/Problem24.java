/*A permutation is an ordered arrangement of objects. 
 * For example, 3124 is one possible permutation of the digits 1, 2, 3 and 4. 
 * If all of the permutations are listed numerically or alphabetically, 
 * we call it lexicographic order. The lexicographic permutations of 0, 1 and 2 are:

012   021   102   120   201   210

What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?*/
package com.euler.initalproblem;

import java.util.LinkedList;
import java.util.List;

import util.NumberUtil;

public class Problem24 {
	
	public static void main(String[] args) {
		int location = 1000000;
		int limit = 10;
		
		List<Integer> digits = new LinkedList<Integer>();
		for(int i=0;i<limit;i++){
			digits.add(i);
		}
		String permutation = NumberUtil.getNthPermutation(location, digits);
		System.out.println(permutation);
		/*for(int i=0;i<totalPermutations;i++){
			String nthPermutation = getNthPermutation(i, digits);
			System.out.println(i+"--"+nthPermutation);
		}*/
	}
	
}
