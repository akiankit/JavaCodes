package com.hackerrank.strings.problems;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MaximumPalindromes {

	static String s1 = null;
	static int limit = 1000000;
	static Map<Long, Long> factorial = new HashMap<>(limit);
	static Map<Long, Long> power = new HashMap<>(limit);
	static char[][] prefixChars = null;

	static void initialize(String s) {
		s1 = s;
		prefixChars = new char[s.length()][256];
		for (int i = 0; i < s.length(); i++) {

		}
	}

	static int answerQuery(int l, int r) {
		String substring = s1.substring(l - 1, r);
		// System.out.println(substring);
		return (int) getMaxLengthPalindromeCount(substring);
	}

	public static Map<Character, Integer> getCharsCount(String s) {
		Map<Character, Integer> charsCount = new HashMap<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int charCount = charsCount.getOrDefault(c, 0);
			charsCount.put(c, charCount + 1);
		}
		return charsCount;
	}

	public static long getMaxLengthPalindromeCount(String s) {
		Map<Character, Integer> charsCount = getCharsCount(s);
		Set<Character> chars = charsCount.keySet();
		// int maxOddCount = 0;
		Map<Character, Integer> charsCount1 = new HashMap<>();
		// Character maxOddChar = null;
		int length = 0;
		int oddCountOccurance = 0;
		for (Character char1 : chars) {
			int charCount = charsCount.get(char1);
			if ((charCount & 1) == 1) {
				oddCountOccurance++;
			}
			charsCount1.put(char1, charCount / 2);
			length = length + charCount / 2;
		}
		// if (maxOddCount != 0) {
		// charsCount1.put(maxOddChar, maxOddCount / 2);
		// length = length + maxOddCount / 2;
		// for (Character char1 : chars) {
		// int charCount = charsCount.get(char1);
		// if (charCount == maxOddCount) {
		// oddCountOccurance++;
		// }
		// }
		// }
		long mod = 1000000007;
		long nominator = getFactorialMod(length, mod);
		long dominator = 1;
		Set<Character> keySet = charsCount1.keySet();
		for (Character character : keySet) {
			Integer count = charsCount1.get(character);
			dominator = (dominator * getFactorialMod(count, mod)) % mod;
		}
		dominator = calculateModPower(dominator, mod - 2, mod);
		if (oddCountOccurance != 0) {
			return (nominator * dominator * oddCountOccurance) % mod;
		}
		return (nominator * dominator) % mod;
		// return length;
	}

	static public long getFactorialMod(long n, long mod) {
		if (factorial.containsKey(n)) {
			return factorial.get(n);
		}
		long fact = 1;
		for (int i = 1; i <= n; i++) {
			fact = (fact * i) % mod;
		}
		if (n < limit) {
			factorial.put(n, fact);
		}
		return fact;
	}

	static public BigInteger calculateModPower(long a, long b, BigInteger mod) {
		BigInteger res = new BigInteger("1");
		BigInteger a1 = new BigInteger(String.valueOf(a));
		while (b != 0) {
			if ((b & 1) == 1)
				res = res.multiply(a1).mod(mod);
			// if (res < 0) {
			// res = res + mod;
			// }
			a1 = a1.multiply(a1).mod(mod);
			b = b >> 1;
		}
		return res;
	}

	static public long calculateModPower(long a, long b, long mod) {
		if (power.containsKey(a)) {
			return power.get(a);
		}
		long maxAllowed = (long) Math.sqrt(Long.MAX_VALUE);
		if (mod >= maxAllowed) {
			return calculateModPower(a, b, new BigInteger(String.valueOf(mod))).longValue();
		}
		long res = 1;
		while (b != 0) {
			if ((b & 1) == 1)
				res = (res * a) % mod;
			// if (res < 0) {
			// res = res + mod;
			// }
			a = (a * a) % mod;
			b = b >> 1;
		}
		if (a < limit) {
			power.put(a, res);
		}
		return res;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String s = in.next();
		initialize(s);
		int q = in.nextInt();
		for (int a0 = 0; a0 < q; a0++) {
			int l = in.nextInt();
			int r = in.nextInt();
			int result = answerQuery(l, r);
			System.out.println(result);
		}
		in.close();
	}
}
