/*Starting with the number 1 and moving to the right in a clockwise direction a 5 by 5 spiral is formed as follows:

21 22 23 24 25
20  7  8  9 10
19  6  1  2 11
18  5  4  3 12
17 16 15 14 13

It can be verified that the sum of the numbers on the diagonals is 101.

What is the sum of the numbers on the diagonals in a 1001 by 1001 spiral formed in the same way?*/
package com.euler.initalproblem;

public class Problem28 {

	public static void main(String[] args) {
		//double pow = Math.pow(1001, 2);
		int sum = 25;
		for(int i=5;i<=1001;i+=2){
			int temp = (int) Math.pow(i, 2);
			while(temp > Math.pow(i-2, 2)) {
				//System.out.println(temp);
				sum = sum+temp;
				temp -=i-1;
			}
		}
		System.out.println(sum);
	}

}
