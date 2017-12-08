package com.hackerrank.misc.problems;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SpecialMultiple {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tests = sc.nextInt();
        for(int i=0;i<tests;i++){
            long N = sc.nextLong();
            System.out.println(getMultiple(9, N));
        }
        sc.close();
    }
    
    private static long getMultiple(long number, long N) {
        if (number % N == 0)
            return number;
        List<Long> list = new LinkedList<Long>();
        list.add(number);
        List<Long> newList = new LinkedList<Long>();
        while (true) {
            for (Long num : list) {
                long a = num * 10;
                if (a % N == 0)
                    return a;
                newList.add(a);
                long b = a + 9;
                if (b % N == 0)
                    return b;
                newList.add(b);
            }
            list = new LinkedList<Long>(newList);
            newList.clear();
        }
        /*
         * if (number % N == 0) return number; long a = number * 10; if (a % N == 0) return a; long
         * b = number * 10 + 9; if (b % N == 0) return b; a = getMultiple(a, N); if (a != 0) {
         * return a; } return getMultiple(b, N);
         */
    }

}
