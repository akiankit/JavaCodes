package com.hackerearth.hiring.seagate;

import java.util.Scanner;

public class ABCStrings {

	static int mod = (int) (Math.pow(10, 9) + 7);

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int tests = sc.nextInt();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tests; i++) {
			int N = sc.nextInt();
			if (N < 3) {
				sb.append(0).append("\n");
			} else {
				long res = calculateModPower(3, N, mod);
				res = res - (3 * calculateModPower(2, N, mod)) % mod;
				if (res < 0) {
					res = mod + res;
				}
				res = (res + 3)%mod;
				sb.append(res).append("\n");
			}
		}
		System.out.println(sb.toString());
		sc.close();
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
}
