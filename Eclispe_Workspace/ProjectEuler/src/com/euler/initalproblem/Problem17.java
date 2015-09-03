/*If the numbers 1 to 5 are written out in words: one, two, three, four, five, then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.

If all the numbers from 1 to 1000 (one thousand) inclusive were written out in words, how many letters would be used?


NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and forty-two) contains 23 letters and 115 (one hundred and fifteen) 
contains 20 letters. 
The use of "and" when writing out numbers is in compliance with British usage.*/ 
package com.euler.initalproblem;

import util.NumberUtil;

public class Problem17 {

	public static void main(String[] args) {
		int sum =0;
		for(int i=1;i<=1000;i++){
			String temp = NumberUtil.getLetterRepresentation(i);
			//System.out.println(temp);
			String[] strings = temp.split("\\s+");
			for (String string : strings) {
				sum+=string.length();
			}
		}
		System.out.println(sum);
	}

}
