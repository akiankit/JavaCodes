package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PRETNUM {
	
	static public List<Long> list_Of_Primes_Less_Than_N(long n) {
		List<Long> primes = new LinkedList<Long>();
		if (n >= 2) {
			primes.add(2l);
			long j = 3;
			if (n > 2) {
				for (; j <= n; j++) {
					boolean isPrime = true;
					long temp = primes.get(0);
					for (int k = 1; temp <= Math.sqrt(j); k++) {
						if (j % temp == 0) {
							isPrime = false;
							break;
						}
						temp = primes.get(k);
					}
					if (true == isPrime && j <= n) {
						primes.add(j);
					}
					if (j > n) {
						break;
					}
				}
			}

		}
		return primes;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			long lValue = scanner.nextLong();
			long rValue = scanner.nextLong();
			List<Long> primes_Less_Than_N = list_Of_Primes_Less_Than_N(rValue);
			System.out.println(primes_Less_Than_N);
			int count = 0;
			for(long j=lValue;j<=rValue;j++){
				long numOfFactos = numOfFactors(j,primes_Less_Than_N);
				if(primes_Less_Than_N.contains(numOfFactos) == true){
					count++;
				}
			}
			System.out.println(count);
		}
		scanner.close();
	}
	
	static public int numOfFactors(long n, List<Long> primes_Less_Than_N) {
		if (n == 1) {
			return 1;
		} else if (primes_Less_Than_N.contains(n)) {
			return 2;
		} else {
			int numOfFactors = 1;
			List<Long> primeFactors = getPrimeFactors(n,primes_Less_Than_N);
			int[] primeFactorsPowers = new int[primeFactors.size()];
			int k = 0;
			for (Long long1 : primeFactors) {
				long temp = n;
				int count = 1;
				while (0 == temp % long1) {
					temp = temp / long1;
					count++;
				}
				primeFactorsPowers[k++] = count;
			}
			if (primeFactors.size() == 0) {
				return 2;
			}
			for (int i : primeFactorsPowers) {
				numOfFactors *= i;
			}
			return numOfFactors;
		}

	}
	
	public static List<Long> getPrimeFactors(long n, List<Long> primes_Less_Than_N) {
		List<Long> primeFactors = new LinkedList<Long>();
		int sqrt = (int) Math.sqrt(n);
		// List<Long> primes_Less_Than_N = list_Of_Primes_Less_Than_N(sqrt);
		for (long temp = 2; temp <= sqrt; temp++) {
			if (n % temp == 0) {
				primeFactors.add(temp);
			}
		}
		List<Long> primeFactors2 = new LinkedList<Long>();
		for (Long long1 : primeFactors) {
			long temp = n / long1;
			if (false == primeFactors.contains(temp) && primes_Less_Than_N.contains(temp)) {
				primeFactors2.add(temp);
			}
		}
		primeFactors.addAll(primeFactors2);
		primeFactors2.clear();
		for (Long long1 : primeFactors) {
			if (primes_Less_Than_N.contains(long1) == true) {
				primeFactors2.add(long1);
			}
		}
		return primeFactors2;
	}

}
