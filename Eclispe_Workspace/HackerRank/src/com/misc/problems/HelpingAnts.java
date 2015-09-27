package com.misc.problems;

import java.util.Scanner;

public class HelpingAnts {
	
	private static long juliaWays(int N, long[] ways, long mod) {
		if(N==1){
			ways[N] =1;
			return 1;
		}
		if (ways[N] != 0)
			return ways[N];
		long total_ways = 0;
		for (int i = 1; i <= N; i++) {
			if (i == 1 || i == N) {
				total_ways = (total_ways + juliaWays(N - 1, ways, mod)) % mod;
				total_ways = (total_ways + 2) % mod;
			} else {
				total_ways = (total_ways + juliaWays(i - 1, ways, mod)) % mod;
				total_ways = (total_ways + juliaWays(N - i, ways, mod)) % mod;
				total_ways = (total_ways + 1) % mod;
			}
		}
		ways[N] = total_ways;
		return total_ways;
	}
	
	static public long calculateModPower(long a, long b, long mod) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) == 1)
                res = (res * a) % mod;
            a = (a * a) % mod;
            b = b >> 1;
        }
        return res;
    }
	
	private static long abhiWays(int n){
		return calculateModPower(2, n, (long) (Math.pow(10, 9)+7));
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();
		for (int i = 0; i < t; i++) {
			int n = sc.nextInt();
			long[] ways = new long[n + 1];
			ways[1] = 1;
			long mod = (long) (Math.pow(10, 9) + 7);
			long wj = juliaWays(n, ways, mod);
			long wa = abhiWays(n - 1);
			long res = wj - wa;
			if (res < 0) {
				res = res + mod;
			}
			System.out.println(res);
		}
		sc.close();
	}
}
