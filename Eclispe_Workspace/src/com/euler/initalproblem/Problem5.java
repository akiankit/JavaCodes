/*2520 is the smallest number that can be divided by each of the numbers from 1 to 10 without any remainder.

What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?*/

//This problem is similar to finding the gcd for n numbers so using the gcd approach.
package com.euler.initalproblem;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Problem5 {

    static Map<Integer, Long> map = new HashMap<Integer, Long>();
    static{
        long lcm = 2;
        map.put(1, (long)1);
        map.put(2, (long)2);
        for (int limit = 3; limit <= 40; limit++) {
            for (int i = 3; i <= limit; i++) {
                lcm = getLCM(lcm, i);
            }
            map.put(limit, lcm);
        }
    }
	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int num = scanner.nextInt();
            System.out.println(map.get(num));
        }
        scanner.close();

    }
	
	static public long getGCD(long a, long b) {
        int gcd = 1;
        if (a == 0 || b == 0)
            return 1;
        else if (a == 1 || b == 1)
            return 1;
        else {
            if (a % b == 0)
                return b;
            else if (b % a == 0)
                return a;
            else if (a > b)
                return getGCD(a % b, b);
            else if (a < b)
                return getGCD(a, b % a);
        }
        return gcd;
    }
	
	static public long getLCM(long lcm2, long b) {
        long lcm = lcm2 * b;
        long gcd = getGCD(lcm2, b);
        lcm = lcm / gcd;
        return lcm;
    }

}
