/*
 * Using names.txt (right click and 'Save Link/Target As...'), a 46K text file
 * containing over five-thousand first names, begin by sorting it into
 * alphabetical order. Then working out the alphabetical value for each name,
 * multiply this value by its alphabetical position in the list to obtain a name
 * score.
 * 
 * For example, when the list is sorted into alphabetical order, COLIN, which is
 * worth 3 + 15 + 12 + 9 + 14 = 53, is the 938th name in the list. So, COLIN
 * would obtain a score of 938 � 53 = 49714.
 * 
 * What is the total of all the name scores in the file?
 */
package com.euler.initalproblem;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.initial.util.FileUtil;

public class Problem22 {

	private static String fileName = "D:\\workspace\\temp\\src\\projectEuler\\problem22Input";

	public static void main(String[] args) throws Exception {
		String[] data = FileUtil.getDataFromFile(fileName);
		List<String> names = new LinkedList<>();
		data = data[0].split(",");
		for (int i = 0; i < data.length; i++) {
			String name = data[i];
			name = name.substring(1, name.length() - 1);
			names.add(name);
		}
		Collections.sort(names);
		long finalScore = 0l;
		for (int i = 0; i < names.size(); i++) {
			finalScore += getScoreOfWord(names.get(i), i + 1);
		}
		System.out.println(finalScore);
	}

	public static long getScoreOfWord(String word, int position) {
		long score = 0l;
		for (int i = 0; i < word.length(); i++) {
			score = score + word.charAt(i) - 64;
		}
		score = score * position;
		return score;
	}

}
