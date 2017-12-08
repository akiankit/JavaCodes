package com.codechef.practice;

import java.util.Scanner;

public class HOTEL {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int result = 1;
			int numGuests = scanner.nextInt();
			
			int[] arrival = new int[numGuests];
			int[] departure = new int[numGuests];
			int minArrival = 0;
			for(int j=0;j<numGuests;j++){
				arrival[j] = scanner.nextInt();
				if(arrival[j]< minArrival){
					minArrival = arrival[j];
				}
			}
			int maxDeparture = 0;
			for(int j=0;j<numGuests;j++){
				departure[j] = scanner.nextInt();
				if(departure[j] > maxDeparture){
					maxDeparture = departure[j];
				}
			}
			int[] maxCoguest = new int[maxDeparture];
			for(int j=minArrival;j<maxDeparture;j++){
				for(int k=0;k<numGuests;k++){
					int t1 = arrival[k];
					int t2 = departure[k];
					if(j>t1 && j < t2){
						maxCoguest[j]++;
					}else if(j==t1){
						maxCoguest[j]++;
					}
				}
				if(maxCoguest[j] > result){
					result = maxCoguest[j];
				}
			}
			System.out.println(result);
		}
		scanner.close();
	}

}
