package com.codechef.practice;

import java.util.Scanner;

public class COMM3 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int maxDistance = scanner.nextInt();
			int x1 = scanner.nextInt();
			int y1 = scanner.nextInt();
			int x2 = scanner.nextInt();
			int y2 = scanner.nextInt();
			int x3 = scanner.nextInt();
			int y3 = scanner.nextInt();
			double distance12 =  getDistance(x1, x2, y1, y2);
			double distance23 = getDistance(x2, x3, y2, y3);
			double distance31 = getDistance(x3, x1, y3, y1);
			boolean isPossible = true;
			/*if(distance12 > maxDistance){
				if((distance23+distance31) > maxDistance){
					isPossible = false;
				}
			}else if(distance23 > maxDistance){
				if((distance12+distance31) > maxDistance){
					isPossible = false;
				}
			}else if(distance31 > maxDistance){
				if((distance12+distance23) > maxDistance){
					isPossible = false;
				}
			}*/
			if((distance12 > maxDistance && distance23 >maxDistance) || (distance31 > maxDistance && distance12 >maxDistance) || (distance23 > maxDistance && distance31 >maxDistance)){
				isPossible = false;
			}
			if(isPossible == false){
				System.out.println("no");
			}else{
				System.out.println("yes");
			}
		}
		scanner.close();
	}
	
	static double getDistance(int x1,int x2,int y1,int y2){
		double distanceX = Math.pow(x2-x1,2);
		double distanceY = Math.pow(y2-y1,2);
		double temp1 = distanceX+distanceY;
		return Math.sqrt(temp1);
	}

}
