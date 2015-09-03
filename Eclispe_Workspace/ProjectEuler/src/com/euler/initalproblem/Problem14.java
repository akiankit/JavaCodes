/*The following iterative sequence is defined for the set of positive integers:

n → n/2 (n is even)
n → 3n + 1 (n is odd)

Using the rule above and starting with 13, we generate the following sequence:

13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1
It can be seen that this sequence (starting at 13 and finishing at 1) contains 10 terms. 
Although it has not been proved yet (Collatz Problem), it is thought that all starting numbers finish at 1.

Which starting number, under one million, produces the longest chain?

NOTE: Once the chain starts the terms are allowed to go above one million.*/
package com.euler.initalproblem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Problem14 {

	static final int CACHE_LENGTH = 16384 * 8;

	static long[] cache = new long[CACHE_LENGTH];

	public static void main(String[] args) throws IOException {
	    Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            long num1 = 1l;
            long num2 = scanner.nextLong();
            System.out.println(Conjecture(num1, num2));
        }
//        System.out.println(Arrays.toString(cache));
		scanner.close();	
	}

	private static long Conjecture(long i, long j) {
		long answer = 0l;
		long num3 = 0l;
		if (i > j) {
			num3 = i;
			i = j;
			j = num3;
		}
		long max_cycle_length = 0;
		for (long k = i; k <= j; k++) {
			long count = 0;
			long cachedConjucture = cachedConjucture(k);
			if (cachedConjucture != 0) {
				count = cachedConjucture;
			} else {
				long n = k;
				while (n != 1) {
					count++;
					if (n % 2 != 0) {
						n = 3 * n + 1;
					} else {
						n = n / 2;
					}
					cachedConjucture = cachedConjucture(n);
					if (cachedConjucture != 0) {
						count += cachedConjucture;
						count--;
						break;
					}
				}
				count++;
				if (k < CACHE_LENGTH) {
					cacheConjucture((int) k, count);
				}
			}
			if (count >= max_cycle_length) {
				answer = k;
				max_cycle_length = count;
			}
		}
		return answer;
	}

	private static long cachedConjucture(long k) {
		if (k < CACHE_LENGTH && cache[(int) k] > 0) {
			return cache[(int) k];
		}
		return 0l;
	}
	
	public static void cacheConjucture(int index, long maxCycleLength) {
        if (index < CACHE_LENGTH && cache[index] == 0) {
            cache[index] = maxCycleLength;
        }
    }

}
