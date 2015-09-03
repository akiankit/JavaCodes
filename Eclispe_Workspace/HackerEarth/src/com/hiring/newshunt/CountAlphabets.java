package com.hiring.newshunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CountAlphabets {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        int[] counts = new int[26];
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) >= 'a' && line.charAt(i) <= 'z') {
				counts[line.charAt(i) - 'a']++;
			} else if (line.charAt(i) >= 'A' && line.charAt(i) <= 'Z') {
				counts[line.charAt(i) - 'A']++;
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<=25;i++){
			sb.append(counts[i]+" ");
		}
		System.out.println(sb.toString());
	}
}
