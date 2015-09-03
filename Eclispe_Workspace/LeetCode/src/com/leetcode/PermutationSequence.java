package com.leetcode;

import java.util.LinkedList;
import java.util.List;

public class PermutationSequence {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String permutation = getPermutation(4, 1);
        System.out.println(permutation);
    }

    public static String getPermutation(int n, int k) {
        int[] factorials = new int[10];
        factorials[0] = 1;
        factorials[1] = 1;
        factorials[2] = 2;
        for (int i = 3; i <= 9; i++) {
            factorials[i] = factorials[i - 1] * (i);
        }
        List<Integer> digits = new LinkedList<Integer>();
        for (int i = 1; i <= n; i++) {
            digits.add(i);
        }
        List<Integer> digits1 = new LinkedList<Integer>();
        digits1.addAll(digits);
        int location = k - 1;
        int digitsCount = n;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < digitsCount; i++) {
            int factorial = factorials[digitsCount - i - 1];
            int index = location / factorial;
            number.append(String.valueOf(digits1.get(index)));
            digits1.remove(index);
            location = location - index * factorial;
        }
        return number.toString();
    }

}
