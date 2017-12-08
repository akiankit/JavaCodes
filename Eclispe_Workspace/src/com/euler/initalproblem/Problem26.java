/*A unit fraction contains 1 in the numerator. The decimal representation of the unit fractions with denominators 2 to 10 are given:

1/2	= 	0.5
1/3	= 	0.(3)
1/4	= 	0.25
1/5	= 	0.2
1/6	= 	0.1(6)
1/7	= 	0.(142857)
1/8	= 	0.125
1/9	= 	0.(1)
1/10	= 	0.1
Where 0.1(6) means 0.166666..., and has a 1-digit recurring cycle. It can be seen that 1/7 has a 6-digit recurring cycle.

Find the value of d < 1000 for which 1/d contains the longest recurring cycle in its decimal fraction part.*/
package com.euler.initalproblem;

import java.util.HashMap;
import java.util.Map;


public class Problem26 {

	public static void main(String[] args) {
		int maxLength = 0;
		int number = 2;
		for(int i=2;i<1000;i++){
			int tempLength = getCycleLength(i);
			System.out.println(i+"-->"+tempLength);
			if(tempLength > maxLength){
				maxLength = tempLength;
				number = i;
			}
		}
		System.out.println(number);
	}
	
	/*private static int getLength(int i){
		StringBuilder result = new StringBuilder();
		result.append(".");
		int length = 0;
		int num = 10;
		while(num < i){
			num = num *10;
			result.append(0);
		}
		int temp2 = num/i;
		num = num%i;
		if(num==0){
			result.append(temp2);
		}else{
			boolean end = false;
			result.append(temp2);
			while(num!= 0){
				int tenCount = 0;
				while(num < i){
					num = num *10;
					if(tenCount != 0){
						int index = result.indexOf(String.valueOf(0));
						if(index == -1){
							result.append(0);
						}else{
							end = true;
							length = result.length()-index;
							break;
						}
					}
					tenCount ++;
				}
				if(end ==true){
					break;
				}
				temp2 = num/i;
				num = num%i;
				int index = result.indexOf(String.valueOf(temp2));
				if(index == -1){
					result.append(temp2);
				}else if(num ==0){
					result.append(temp2);
					break;
				}else{
					length = result.length()-index;
					break;
				}
			} 
		}
		return length;
	}
*/
	
	private static int getCycleLength(int n) {
		Map<Integer,Integer> stateToIter = new HashMap<Integer,Integer>();
		int state = 1;
		int iter = 0;
		while (!stateToIter.containsKey(state)) {
			stateToIter.put(state, iter);
			state = state * 10 % n;
			iter++;
		}
		return iter - stateToIter.get(state);
	}
	
}
