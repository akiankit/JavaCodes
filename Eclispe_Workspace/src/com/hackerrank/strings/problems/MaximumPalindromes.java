package com.hackerrank.strings.problems;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * <a href=
 * "https://www.hackerrank.com/contests/hourrank-25/challenges/maximum-palindromes/submissions">
 * Problem link</a>
 *
 * @author ankit
 */
public class MaximumPalindromes {

	static String s1 = null;
	static int limit = 100000;
	static long[] factorial = new long[limit + 1];
	static Map<Long, Long> power = new HashMap<>(limit);
	static int[][] charsArray = null;
	static int numCount = 26;
	static int mod = 1000000007;

	static {
		initFactorials();
	}

	static void initialize(String s) {
		s1 = s;
		charsArray = new int[s1.length() + 1][numCount];
		charsArray[1][getSmallCharIndex(s1.charAt(0))]++;
		for (int i = 1; i < s1.length(); i++) {
			for (int j = 0; j < numCount; j++) {
				charsArray[i + 1][j] = charsArray[i][j];
			}
			charsArray[i + 1][getSmallCharIndex(s1.charAt(i))]++;
		}
		// ArraysUtil.print2DArray(charsArray);
	}

	private static void initFactorials() {
		long fact = 1;
		factorial[0] = 1;
		for (int i = 1; i <= limit; i++) {
			fact = fact * i % mod;
			factorial[i] = fact;
		}
		for (int i = 0; i < 10; i++) {
			// System.out.println(factorial[i]);
		}
	}

	public static int getSmallCharIndex(char c) {
		return c - 'a';
	}

	public static char getCharFromIndex(int i) {
		return (char) ('a' + i);
	}

	static int answerQuery(int l, int r) {
		// System.out.println("l=" + l + ",r=" + r);
		// String substring = s1.substring(l - 1, r);
		Map<Character, Integer> chars = getCharsCount(l, r);
		// System.out.print("optimized:");
		// System.out.println(chars);
		// System.out.print("brute force:");
		// System.out.println(getCharsCount(substring));
		// System.out.println(substring);
		return (int) getMaxLengthPalindromeCount(chars);
		// return 0;
	}

	private static Map<Character, Integer> getCharsCount(int l, int r) {
		int[] chars = new int[numCount];
		l = l - 1;
		for (int i = 0; i < numCount; i++) {
			chars[i] = charsArray[r][i] - charsArray[l][i];
		}
		// chars[getSmallCharIndex(s1.charAt(r - 1))]++;
		// System.out.println(Arrays.toString(chars));
		Map<Character, Integer> charsMap = new HashMap<>();
		for (int i = 0; i < numCount; i++) {
			if (chars[i] > 0) {
				charsMap.put(getCharFromIndex(i), chars[i]);
			}
		}
		return charsMap;
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
		return getMaxLengthPalindromeCount(charsCount);
	}

	public static long getMaxLengthPalindromeCount(Map<Character, Integer> charsCount) {
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
		long nominator = getFactorialMod(length, mod);
		long dominator = 1;
		Set<Character> keySet = charsCount1.keySet();
		for (Character character : keySet) {
			Integer count = charsCount1.get(character);
			dominator = (dominator * getFactorialMod(count, mod)) % mod;
		}
		dominator = calculateModPower(dominator, mod - 2, mod);
		// System.out.println("oddCountOccurance:" + oddCountOccurance);
		if (oddCountOccurance != 0) {
			return (nominator * dominator * oddCountOccurance) % mod;
		}
		return (nominator * dominator) % mod;
		// return length;
	}

	static public long getFactorialMod(long n, long mod) {
		// System.out.println("n:" + n + ",factorial:" + factorial[(int) n]);
		return factorial[(int) n];
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
		// long maxAllowed = (long) Math.sqrt(Long.MAX_VALUE);
		// if (mod >= maxAllowed) {
		// return calculateModPower(a, b, new
		// BigInteger(String.valueOf(mod))).longValue();
		// }
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
		// if (a < limit) {
		// power.put(a, res);
		// }
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
