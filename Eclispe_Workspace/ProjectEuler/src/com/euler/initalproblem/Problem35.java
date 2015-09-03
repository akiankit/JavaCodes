package com.euler.initalproblem;

import java.util.Arrays;
import java.util.List;

import util.NumberUtil;

/*The number, 197, is called a circular prime because all rotations of the digits: 197, 971, and 719, are themselves prime.

 There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, and 97.

 How many circular primes are there below one million?*/

public class Problem35 {

    static Object[] primes_Less_Than_N;
    static {
        List<Long> primes_Less_Than_N2 = NumberUtil.getListOfPrimesLessThanN(10000000);
        primes_Less_Than_N = primes_Less_Than_N2.toArray();
    }

    public static void main(String[] args) {
        int count = 0;
        for (int i = 0; i < primes_Less_Than_N.length; i++) {
            if (isItACircularPrime((Long)primes_Less_Than_N[i])) {
                // System.out.println(primes_Less_Than_N[i]);
                count ++;
            }
        }
        System.out.println(count);
    }

    private static boolean isItACircularPrime(Long number) {
        if (number == 2)
            return true;
        /*if (NumberUtil.doesNumberContainEvenNumber(number) == true)
            return false;*/
        long[] allCircularRotations = NumberUtil.getAllCircularRotations(number);
        for (int i = 0; i < allCircularRotations.length; i++) {
            if (Arrays.binarySearch(primes_Less_Than_N, allCircularRotations[i]) < 0)
                return false;
        }
        return true;
    }

}
