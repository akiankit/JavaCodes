package com.codechef.practice;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class VOTERS {

	public static void main(String[] args) {
		Scanner scanner  = new Scanner(System.in);
		int length1 = scanner.nextInt();
		int length2 = scanner.nextInt();
		int length3 = scanner.nextInt();
		Map<Integer,Integer> finalList = new TreeMap<Integer,Integer>();
		for(int i=0;i<length1+length2+length3;i++){
			int id = scanner.nextInt();
			int count = 1;
			if(finalList.containsKey(id)){
				count = finalList.get(id)+1;
			}
			finalList.put(id, count);
		}
		scanner.close();
		Set<Integer> keySet = finalList.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		int listSize = 0;
		while(iterator.hasNext()){
			int key = iterator.next();
			int value = finalList.get(key);
			if(value >1){
				listSize ++;
			}
		}
		System.out.println(listSize);
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			int key = iterator.next();
			int value = finalList.get(key);
			if(value >= 2){
				System.out.println(key);
			}
		}
	}

}
