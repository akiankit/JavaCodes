/*Find the sum of all the multiples of 3 or 5 below 1000.*/
package com.euler.initalproblem;

public class problem1 {

	public static void main(String[] args) {
		int sum = 0;
		for(int i=3;i<1000;i++){
			if(0==i%3|| 0==i%5){
				sum +=i;
			}
		}
		System.out.println(sum);
	}

}
