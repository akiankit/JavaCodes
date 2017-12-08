package com.codechef.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TIDRICE {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int votesCount = scanner.nextInt();
			Map<String,Integer> userVotes = new HashMap<String,Integer>();
			for(int j=0;j<votesCount;j++){
				String user = scanner.next();
				String vote = scanner.next();
				if(vote.equalsIgnoreCase("+")){
					userVotes.put(user, 1);
				}else{
					userVotes.put(user, -1);
				}
			}
			Set<String> keySet = userVotes.keySet();;
			int finalVote = 0;
			for (String string : keySet) {
				finalVote += userVotes.get(string);
			}
			System.out.println(finalVote);
		}
		scanner.close();
	}

}
