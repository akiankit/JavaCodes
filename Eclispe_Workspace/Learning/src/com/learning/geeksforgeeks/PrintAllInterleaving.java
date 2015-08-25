package com.strings;

import java.util.HashSet;
import java.util.Set;

public class PrintAllInterleaving {
	
	public static void main(String[] args) {
		System.out.println(allInterLeaving("AB", "CA"));
	}
	
	//Complexity is very high
	private static Set<String> allInterLeaving(String s1, String s2) {
		Set<String> resultSet = new HashSet<String>();
		for(int i=0;i<=s1.length();i++) {
			String str1 = s1.subSequence(0, i).toString();
			String str2 = s1.substring(i);
			for(int j=0;j<s2.length();j++) {
				String str3 = s2.subSequence(0, j).toString();
				String str4 = s2.substring(j);
				resultSet.add(str1+str3+str2+str4);
				resultSet.add(str3+str1+str4+str2);
			}
		}
		return resultSet;
	}
}
