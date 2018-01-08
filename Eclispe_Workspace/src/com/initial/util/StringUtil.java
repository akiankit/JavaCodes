package com.initial.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class StringUtil {

	/**
	 * Checks is given string characters can be rearranged to make a palindrome.
	 *
	 * @param s
	 *            -- string for which this checks needs to be done
	 * @return yes if palindrome is possible, no if not
	 */
	public static boolean isAPalindromePermutationPossible(String s) {
		int length = s.length();
		char[] chars = s.toCharArray();
		int[] nums = new int[256];
		for (int i = 0; i < length; i++) {
			nums[chars[i]]++;
		}
		int oddCounts = 0;
		for (int i = 0; i < 256; i++) {
			if ((nums[i] & 1) == 1) {
				oddCounts++;
			}
		}
		return ((length & 1) == 0) ? oddCounts == 0 : oddCounts == 1;
	}

	/**
	 * Returns set of palindrome permutations that can be formed with the passed
	 * string S.</br>
	 * Returns empty set if palindrome permutation is not possible.
	 *
	 * @param s
	 *            -- input string
	 * @return
	 */
	public static Set<String> getAllPalindromePermutations(String s) {
		if (!isAPalindromePermutationPossible(s)) {
			return Collections.emptySet();
		}
		Map<Character, Integer> charsCount = getCharsCount(s);
		Set<Character> charsSet = charsCount.keySet();
		StringBuilder permutation = new StringBuilder();
		StringBuilder charWithOddCount = new StringBuilder();
		for (Character character : charsSet) {
			int charCount = charsCount.get(character);
			for (int i = 0; i < charCount / 2; i++) {
				permutation.append(character);
			}
			if ((charCount & 1) != 0) {
				charWithOddCount.append(character);
			}
		}
		// System.out.println(s);
		Set<String> allPermutations = getAllPermutations(permutation.toString());
		// System.out.println(allPermutations);
		Set<String> palindromes = new HashSet<>();
		for (String permutation1 : allPermutations) {
			palindromes.add(permutation1 + charWithOddCount.toString() + reverseString(permutation1));
		}
		palindromes = new TreeSet<>(palindromes);
		return palindromes;
	}

	public static int maxLengthOfPalindrome(String s) {
		Map<Character, Integer> charsCount = getCharsCount(s);
		Set<Character> chars = charsCount.keySet();
		int maxOddCount = 0;
		int length = 0;
		for (Character char1 : chars) {
			int charCount = charsCount.get(char1);
			if ((charCount & 1) == 1) {
				if (maxOddCount < charCount) {
					maxOddCount = charCount;
				}
			} else {
				length += charCount;
			}
		}
		length += maxOddCount;
		return length;
	}

	public static long getMaxLengthPalindromeCount(String s) {
		Map<Character, Integer> charsCount = getCharsCount(s);
		Set<Character> chars = charsCount.keySet();
		int maxOddCount = 0;
		Map<Character, Integer> charsCount1 = new HashMap<>();
		Character maxOddChar = null;
		int length = 0;
		for (Character char1 : chars) {
			int charCount = charsCount.get(char1);
			if ((charCount & 1) == 1) {
				if (maxOddCount < charCount) {
					maxOddCount = charCount;
					maxOddChar = char1;
				}
			} else {
				charsCount1.put(char1, charCount / 2);
				length = length + charCount / 2;
			}
		}
		int oddCountOccurance = 0;
		if (maxOddCount != 0) {
			charsCount1.put(maxOddChar, maxOddCount / 2);
			length = length + maxOddCount / 2;
			for (Character char1 : chars) {
				int charCount = charsCount.get(char1);
				if (charCount == maxOddCount) {
					oddCountOccurance++;
				}
			}
		}
		long mod = 1000000007;
		long nominator = NumberUtil.getFactorialMod(length, mod);
		long dominator = 1;
		Set<Character> keySet = charsCount1.keySet();
		for (Character character : keySet) {
			Integer count = charsCount1.get(character);
			dominator = (dominator * NumberUtil.getFactorialMod(count, mod)) % mod;
		}
		dominator = NumberUtil.calculateModPower(dominator, mod - 2, mod);
		if (oddCountOccurance != 0) {
			return (nominator * dominator * oddCountOccurance) % mod;
		}
		return (nominator * dominator) % mod;
		// return length;
	}

	/**
	 * Returns {@link Set} of all string permutations. The idea is to
	 *
	 * <pre>
	 * first generated all circular rotations string
	 * Then for each roation divide string into two parts, 1st with 1 char(s1) and 2nd with rest of them(s2)
	 * Genrate all permutations for s2 by recursively calling same fucntion.
	 * Append s1 to all permutations for s2
	 * If s2 is of length == 1 then no other permutation possible apart from itself.
	 * To make it slightly more smooth save generated permutations for a string in map.
	 * </pre>
	 *
	 * <pre>
	 * </pre>
	 *
	 * @param s
	 * @return
	 */
	public static Set<String> getAllPermutations(String s) {
		return getAllPermutations(s, null);
	}

	/**
	 * Reverses string passed</br>
	 * </br>
	 * eg:
	 * <li>abc => cba</li>
	 * <li>aabb=>bbaa</li>
	 * 
	 * @param s
	 * @return
	 */
	public static String reverseString(String s) {
		char[] charArray = s.toCharArray();
		int n = charArray.length;
		for (int i = 0; i < n / 2; i++) {
			char a = charArray[i];
			charArray[i] = charArray[n - i - 1];
			charArray[n - i - 1] = a;
		}
		return new String(charArray);
	}

	/**
	 * Returns {@link Set} of all string permutations. The idea is to
	 *
	 * <pre>
	 * first generated all circular rotations string
	 * Then for each roation divide string into two parts, 1st with 1 char(s1) and 2nd with rest of them(s2)
	 * Genrate all permutations for s2 by recursively calling same fucntion.
	 * Append s1 to all permutations for s2
	 * If s2 is of length == 1 then no other permutation possible apart from itself.
	 * To make it slightly more smooth save generated permutations for a string in map.
	 * </pre>
	 *
	 * <pre>
	 * </pre>
	 *
	 * @param s
	 * @param permutationMap
	 * @return
	 */
	public static Set<String> getAllPermutations(String s, Map<String, Set<String>> permutationMap) {
		Set<String> permutations = new HashSet<>();
		if (permutationMap == null) {
			permutationMap = new HashMap<>();
		}
		if (permutationMap.containsKey(s)) {
			// System.out.println("Getting value from map for:" + s);
			return permutationMap.get(s);
		}
		if (s.length() == 1) {
			permutations.add(s);
			return permutations;
		}
		Set<String> rotations = getAllCircularRotations(s);
		for (String rotation : rotations) {
			String s1 = rotation.substring(0, 1);
			String s2 = rotation.substring(1);
			Set<String> allPermutations = getAllPermutations(s2, permutationMap);
			for (String permutation : allPermutations) {
				permutations.add(s1 + permutation);
			}
		}
		if (s.length() <= 8) {
			permutationMap.put(s, permutations);
		}
		return permutations;
	}

	/**
	 * Gets {@link Map} with keys as character and value as count of occurances
	 * of the character
	 * 
	 * <pre>
	 * eg: S="aabbccccde" returns below Map:
	 * a =>2
	 * b=>2
	 * c=>4
	 * d=>1
	 * e=>1
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static Map<Character, Integer> getCharsCount(String s) {
		Map<Character, Integer> charsCount = new HashMap<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int charCount = charsCount.getOrDefault(c, 0);
			charsCount.put(c, charCount + 1);
		}
		return charsCount;
	}

	/**
	 * Gets string=s which can be generated by rotating the strings.</br>
	 * The idea is to append string to same string and generated all substring
	 * of intial string length. Idea taken from geeksforgeeks.
	 *
	 * <pre>
	 * ex: for abc, strings genetated from rotations are: abc, bca, cab
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static Set<String> getAllCircularRotations(String s) {
		int length = s.length();
		Set<String> rotations = new HashSet<>(length);
		s = s + s;
		for (int i = 0; i < length; i++) {
			rotations.add(s.substring(i, i + length));
		}
		return rotations;
	}

	public static void main(String[] args) {
		Map<String, Set<String>> map = new HashMap<>();
		// Set<String> allPermutations1 = getAllPermutations("abac", map);
		String s = null;
		s = "aabbcadad";
		// s = "ababc";
		s = "aaaaabbb";
		System.out.println(maxLengthOfPalindrome(s));
		System.out.println(getMaxLengthPalindromeCount(s));
	}
}
