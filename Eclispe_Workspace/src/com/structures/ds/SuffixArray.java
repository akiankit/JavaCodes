
package com.structures.ds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.initial.util.ArraysUtil;

public class SuffixArray {

	public static void getLCP(String s) {
		/**
		 * This is required because without suffix array we can not find out LCP
		 * array.
		 */
		int[] suffixArray = getSuffixArrayIndexes(s);
		System.out.println(Arrays.toString(suffixArray));
		int n = suffixArray.length;
		int[] indexesMap = new int[n];
		/**
		 * This is required because we have to compare between two adjacent
		 * suffixes. So we have to start from begining and and we have to find
		 * out indexes in string for adjacent suffixes. Using this we can find
		 * out that.
		 */
		for (int i = 0; i < n; i++) {
			indexesMap[suffixArray[i]] = i;
		}
		System.out.println(Arrays.toString(indexesMap));
		int[] lcp = new int[n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			/**
			 * For every i find out the index in suffixArray for the suffix
			 * starting at ith index in String.
			 */
			int suffixIndex = indexesMap[i];
			/**
			 * if suffix is first then there is no point of checking for
			 * previsous one. Because suffix[-1] can't exist
			 */
			if (suffixIndex == 0) {
				k = 0;
				continue;
			}
			/**
			 * j is the starting position for Suffix next to i.
			 */
			int j = suffixArray[suffixIndex - 1];
			System.out.println("Comparision between:" + i + "," + j);
			System.out.println(s.substring(i) + "," + s.substring(j));
			/**
			 * Check for k character because at this point we have ensured that
			 * at least k characters match
			 */
			while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
				k++;
			}
			lcp[suffixIndex] = k;
			/**
			 * if K > 0 then we need to reduce k because we are decreasing size
			 * of suffix
			 */
			if (k > 0)
				k--;
			System.out.println(Arrays.toString(lcp));
		}
		System.out.println(Arrays.toString(lcp));
	}

	public static void main(String[] args) {
		String s = "abaababbabbb";
		s = "banana";
		getLCP(s);
	}

	private static int[] getSuffixArrayIndexes(String s) {
		Suffix[] suffixArray = getSuffixArray(s);
		int[] indexes = new int[suffixArray.length];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = suffixArray[i].getSuffixIndex();
		}
		return indexes;
	}

	public static Suffix[] getSuffixArray(String s) {
		Suffix[] suffixArray = new Suffix[s.length()];
		for (int i = 0; i < s.length(); i++) {
			suffixArray[i] = new Suffix(s.substring(i));
			suffixArray[i].setSuffixIndex(i);
		}
		Arrays.sort(suffixArray);
		// System.out.println(Arrays.toString(suffixArray));
		for (int i = 2; i <= 8; i = i * 2) {
			// ArraysUtil.print1DArray(suffixArray, System.lineSeparator());

			updateCurrentRanks(suffixArray);
			// ArraysUtil.print1DArray(suffixArray, System.lineSeparator());

			updateNextRanks(suffixArray, i);
			// ArraysUtil.print1DArray(suffixArray, System.lineSeparator());

			Arrays.sort(suffixArray);
			// ArraysUtil.print1DArray(suffixArray, System.lineSeparator());
		}
		ArraysUtil.print1DArray(suffixArray, System.lineSeparator());
		return suffixArray;
	}

	private static void updateCurrentRanks(Suffix[] suffixArray) {
		int length = suffixArray.length;
		int rank = 0;
		int prevCurrentRank = suffixArray[0].getCurrentRank();
		suffixArray[0].setCurrentRank(0);
		// int prevNextRank = suffixArray[0].getNextRank();
		for (int i = 1; i < length; i++) {
			Suffix suffix = suffixArray[i];
			int currentRank = suffix.getCurrentRank();
			if (prevCurrentRank == currentRank) {
				Suffix prevSuffix = suffixArray[i - 1];
				if (prevSuffix.getNextRank() != suffix.getNextRank()) {
					prevCurrentRank = suffix.getCurrentRank();
					suffix.setCurrentRank(++rank);
				} else {
					suffix.setCurrentRank(rank);
				}
			} else {
				prevCurrentRank = suffix.getCurrentRank();
				suffix.setCurrentRank(++rank);
			}
		}
	}

	private static void updateNextRanks(Suffix[] suffixArray, int next) {
		int length = suffixArray.length;
		Map<Integer, Suffix> suffixIndicesMap = new HashMap<>(length);
		for (int i = 0; i < length; i++) {
			Suffix suffix = suffixArray[i];
			suffixIndicesMap.put(suffix.getSuffixIndex(), suffix);
		}
		// System.out.println(suffixIndicesMap);
		for (int i = 0; i < length; i++) {
			Suffix suffix = suffixArray[i];
			int suffixIndex = suffix.getSuffixIndex();
			int nextSuffixIndex = suffixIndex + next;
			if (nextSuffixIndex >= length) {
				suffix.setNextRank(-1);
			} else {
				int nextRank = suffixIndicesMap.get(nextSuffixIndex).getCurrentRank();
				suffix.setNextRank(nextRank);
			}
		}
	}

}

class Suffix implements Comparable<Suffix> {

	private String string;
	private int suffixIndex;
	private int currentRank;
	private int nextRank;

	public Suffix(String s) {
		this.setString(s);
		currentRank = s.charAt(0) - 'a';
		if (s.length() > 1) {
			nextRank = s.charAt(1) - 'a';
		} else {
			nextRank = -1;
		}
	}

	@Override
	public int compareTo(Suffix o) {
		if (this.getCurrentRank() != o.getCurrentRank()) {
			return this.getCurrentRank() - o.getCurrentRank();
		}
		return this.getNextRank() - o.getNextRank();
	}

	/**
	 * @return the currentRank
	 */
	public int getCurrentRank() {
		return currentRank;
	}

	/**
	 * @param currentRank
	 *            the currentRank to set
	 */
	public void setCurrentRank(int currentRank) {
		this.currentRank = currentRank;
	}

	/**
	 * @return the nextRank
	 */
	public int getNextRank() {
		return nextRank;
	}

	/**
	 * @param nextRank
	 *            the nextRank to set
	 */
	public void setNextRank(int nextRank) {
		this.nextRank = nextRank;
	}

	/**
	 * @return the string
	 */
	public String getString() {
		return string;
	}

	/**
	 * @param string
	 *            the string to set
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * @return the suffixIndex
	 */
	public int getSuffixIndex() {
		return suffixIndex;
	}

	/**
	 * @param suffixIndex
	 *            the suffixIndex to set
	 */
	public void setSuffixIndex(int suffixIndex) {
		this.suffixIndex = suffixIndex;
	}

	@Override
	public String toString() {
		return "[" + getString() + " (Index:" + getSuffixIndex() + "(" + currentRank + "," + nextRank + "))]";
	}

}
