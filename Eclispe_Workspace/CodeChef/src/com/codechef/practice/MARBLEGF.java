package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MARBLEGF {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int nValue = scanner.nextInt();
		int qValue = scanner.nextInt();
		
		//List<Integer> answers = new LinkedList<Integer>();
		
		int[] marbles = new int[nValue];
		int[] sumMarbles = new int[nValue];
		
		for(int i=0;i<nValue;i++){
			marbles[i] = scanner.nextInt();
			if(i == 0){
				sumMarbles[i] = marbles[i];
			}else{
				sumMarbles[i] = sumMarbles[i-1]+ marbles[i];
			}
		}
		List<Character> operations = new LinkedList<Character>();
		List<Integer> indexes = new LinkedList<Integer>();
		List<Integer> values = new LinkedList<Integer>();
		for(int i=0;i<qValue;i++){
			String next = scanner.next();
			char operation = next.charAt(0);
			int index = scanner.nextInt();
			int marbleValue = scanner.nextInt();
			if(operation == 'G' || operation == 'T'){
				operations.add(operation);
				indexes.add(index);
				values.add(marbleValue);
			}else{
				int sum = 0;
				for (int j=0;j<operations.size();j++) {
					if(operations.get(j) == 'G'){
						marbles[indexes.get(j)] += values.get(j); 
					}else{
						marbles[indexes.get(j)] -= values.get(j);
					}
				}
				for(int j=index;j<=marbleValue;j++){
					sum +=marbles[j];
				}
				System.out.println(sum);
			}
		}
		scanner.close();
	}

}
