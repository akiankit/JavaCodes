/*The Fibonacci sequence is defined by the recurrence relation:

Fn = Fn−1 + Fn−2, where F1 = 1 and F2 = 1.
Hence the first 12 terms will be:

F1 = 1
F2 = 1
F3 = 2
F4 = 3
F5 = 5
F6 = 8
F7 = 13
F8 = 21
F9 = 34
F10 = 55
F11 = 89
F12 = 144
The 12th term, F12, is the first term to contain three digits.

What is the first term in the Fibonacci sequence to contain 1000 digits?*/
package com.euler.initalproblem;

import java.util.List;

import util.NumberUtil;


//TODO need to modify .Just getting the answer right now.
public class Problem25 {

	public static void main(String[] args) {
		List<String> terms = NumberUtil.getFibonacciSeriesNTerms(5000);
		int i=0;
		for(i=5000-1;i>=0;i--){
			String term = terms.get(i);
			if(term.length() < 1000){
				break;
			}
		}
		System.out.println(i+2);
	}

}
