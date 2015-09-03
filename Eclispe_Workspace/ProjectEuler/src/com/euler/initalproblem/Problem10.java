/*The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.

Find the sum of all the primes below two million.*/
package com.euler.initalproblem;

import util.NumberUtil;

public class Problem10 {

	public static void main(String[] args) {
		long limit = 2000000;
		//long limit = 20;
		long sum = 0l;
		long[] primes = NumberUtil.getListOfFirstNPrimes(1000000);
		
		System.out.println(primes[primes.length -1]);
		for(int i=0;primes[i] < limit;i++){
			//System.out.println(nthPrime);
			sum +=primes[i];
		} 
		System.out.println(sum);
	}

}
