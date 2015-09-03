/*We want to find the smallest area where a specified set of words appear at least each given times. 

That is, the input contains a number of pairs (b, x), where b is the identity of a word and x is the location in the document 
that the word identified by b appears. 
For example, if the content of a document is â€œTom Loves Jane as Jane Loves Tomâ€? and we are interested in the words Tom and Jane 
then the document can be represented as follows (the identity of Tom is 1 and the identity of Jane is 2):
(1, 1) (2, 3) (2, 5) (1, 7)
	
A more complicated input might look like the following.
(1, 3) (2, 5) (1, 6) (1, 7) (1, 8) (3, 10) (1, 18) (1, 19) (3, 25) (2, 30)

In this example, we are interested in 3 different words (i.e., we ignore all other words in the document) and the word identified 
by 1 appears at locations 3, 6, 7, 8, 18, and 19 and the word identified by 2 appears at locations 5 and 30, and so on. 

You may assume that the pairs are given in the increasing order of x. Now assume that the word identified by 1 has to appear at least
2 times in the wanted area and the words identified by 2 and 3 have to appear at least once in the wanted area. 

One can see that the smallest area where all three words appear at least each specified times is from the 5th to the 10th locations 
and the size is 10 - 5 + 1 = 6. 
(In the input, it might look like the area from the 18th to the 30th locations is smaller because it contains only 4 pairs, 
but if you consider the original document this is larger than the previous one.) As has been shown just now, the size of an area 
from location s to location t is defined to be t - s + 1.

Given an input as specified above, write a program that computes the size of the smallest area that contains all of the words 
appearing at least each specified times.
[Input]
 the first line has two integers N (the number of pairs, no more than 1,000,000) and K (the number of interested words, no more than 1,000). 
The words are identified by integers from 1 to K. 
The next K lines contain one integer each, specifying the least number of times that a word has to appear in an area, 
in the order of identity of words. 
The next N lines contain two integers each specifying a pair. The first integer is the identity of the word 
and the second one is the location of the appearance of the word. 
The N pairs are given in the increasing order of the location. The value of the locations are no larger than 5,000,000.
10 3                           â†? Starting Case 1
2
1
1
1 3
2 5
1 6
1 7
1 8
3 10
1 18
1 19
3 25
2 30 

[Output]
6
output contains the size of the smallest area matching the condition of the input. If there is no such area, then the second line contains -1.

*/
package com.samsung.test;

import java.util.Scanner;

public class Smallest_Area {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int locationCount = scanner.nextInt();
		int numCount = scanner.nextInt();
		int nums[] = new int[numCount];
		int location_b[] = new int [locationCount];
		int location_x[] = new int [locationCount];
		int temp_b[] = new int[locationCount];
		int repetitionSum = 0;
		for(int i=0;i<numCount;i++){
			nums[i] = scanner.nextInt();
		}
		repetitionSum = sumArray(nums);
		for(int i=0;i<locationCount;i++){
			location_b[i] = scanner.nextInt();
			location_x[i] = scanner.nextInt();
		}
		scanner.close();
		for(int i=0;i<locationCount;i++){
			temp_b[i] = location_b[i];
			int sumTemp = sumArray(temp_b);
			if(sumTemp >= repetitionSum){
				
			}
		}
	}

	private static int sumArray(int[] nums) {
		int sum = 0;
		for(int i=0;i<nums.length && nums[i] !=0;i++){
			sum = sum+nums[i];
		}
		return sum;
	}

}
