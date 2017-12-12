package com.euler.initalproblem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.initial.util.FileUtil;

public class Problem42 {

	public static void main(String[] args) throws Exception {
		String[] dataFromFile = FileUtil.getDataFromFile(
		                "/home/ankit/WORKSPACE/STS_WORKSPACE/MyCode/Eclispe_Workspace/src/com/euler/initalproblem/problem42_input.txt");
		dataFromFile = dataFromFile[0].replaceAll("\"", "").split(",");
		System.out.println(Arrays.toString(dataFromFile));
		Set<Integer> sequence = new HashSet<>();
		int prev = 0;
		for (int i = 1; prev < 500; i++) {
			int value = i + prev;
			sequence.add(value);
			prev = value;
		}
		Map<String, Integer> wordValues = new HashMap<>(dataFromFile.length);
		int count = 0;
		for (String word : dataFromFile) {
			int value = getValue(word);
			// maxValue = Math.max(value, maxValue);
			if (sequence.contains(value)) {
				count++;
			}
			wordValues.put(word, value);
		}
		System.out.println(count);
	}

	private static int getValue(String word) {
		int value = 0;
		for (int i = 0; i < word.length(); i++) {
			value = value + 1 + (word.charAt(i) - 'A');
		}
		return value;
	}

}
