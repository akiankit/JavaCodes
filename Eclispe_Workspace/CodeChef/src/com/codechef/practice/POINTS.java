package com.codechef.practice;

import java.util.Arrays;
import java.util.Scanner;

public class POINTS implements Comparable<POINTS>{
	
	int x;
	int y;
	
	public POINTS(int x,int y) {
		this.x=x;
		this.y=y;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for(int i=0;i<testCases;i++){
			double dist = 0.00;
			int numOfPoints = scanner.nextInt();
			POINTS[] points  = new POINTS[numOfPoints];
			for(int j=0;j<numOfPoints;j++){
				int x = scanner.nextInt();
				int y= scanner.nextInt();
				points[j]=new POINTS(x, y);
			}
			Arrays.sort(points);
			POINTS startPoint = points[0];
			for (POINTS points2 : points) {
				System.out.println(points2);
			}
			for (POINTS points2 : points) {
				POINTS endPoint = points2;
				double distanceX = Math.pow(endPoint.x-startPoint.x,2);
				double distanceY = Math.pow(endPoint.y-startPoint.y,2);
				double temp1 = distanceX+distanceY;
				dist = dist + Math.sqrt(temp1);
				//System.out.println("distanceX="+distanceX+" distanceY="+distanceY+" temp1="+temp1);
				System.out.println(dist);
				startPoint = endPoint;
			}
			System.out.printf("%.2f",dist);//Show output up to 2 decimal places
			System.out.println();
		}
		scanner.close();
	}
	
	public String toString(){
		return this.x+","+this.y;
	}

	public int compareTo(POINTS pointB) {
		int diff = this.x-pointB.x;
		if(diff == 0){
			return pointB.y-this.y;
		}
		return diff;
	}

}
