package com.learning.geeksforgeeks;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


class Solution{
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int query = scanner.nextInt();
		List<Data> list = new LinkedList<Data>();
		for(int i=0;i<query;i++) {
			String word = scanner.next();
			int num = scanner.nextInt();
			if(word.equalsIgnoreCase("add")){
				binarySearch(list, 0, list.size(), num);
			}else if(word.equalsIgnoreCase("del")){
				
			}else{
				
			}
		}
		scanner.close();
	}
	
	private static int binarySearch(List<Data> list, int start, int end, int target) {
		if(start > end)
			return -1;
		int mid = (start+end)/2;
		if(list.get(mid).value == target) {
			return mid;
		}else if(list.get(mid).value > target) {
			return binarySearch(list, start, mid-1, target);
		}else{
			if(list.get(mid+1).value> target)
				return mid;
			else
				return binarySearch(list, mid+1, end, target);
		}
	}
	
}

class Data{
	int value;
	int count;
	int size;
	
	public Data(int value, int count) {
		this.value = value;
		this.count= count;
	}
}