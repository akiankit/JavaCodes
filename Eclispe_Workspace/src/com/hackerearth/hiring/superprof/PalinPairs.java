package com.hackerearth.hiring.superprof;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PalinPairs {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int num = scanner.nextInt();
		String[] input = new String[num];
		Map<String, Integer> map = new HashMap<String, Integer>();
		int count = 0;
		for (int i = 0; i < input.length; i++) {
			input[i] = scanner.next();
		}
		boolean[] visited = new boolean[input.length];
		for (int i = 0; i < input.length; i++) {
			if (visited[i] == true)
				continue;
			else if (map.containsKey(input[i])) {
				count = count + map.get(input[i]);
			} else {
				visited[i] = true;
				String reverse = new StringBuffer(input[i]).reverse()
						.toString();
				int tempCount = 0;
				for (int j = i + 1; j < input.length; j++) {
					if (reverse.equalsIgnoreCase(input[j])) {
						visited[j] = true;
						tempCount++;
					}
				}
				map.put(input[i], tempCount);
				count = count + tempCount;
			}
		}
		System.out.println(count);
		scanner.close();
	}
}
