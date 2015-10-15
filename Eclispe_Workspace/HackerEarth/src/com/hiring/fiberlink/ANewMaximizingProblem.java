package com.hiring.fiberlink;

import java.util.HashSet;
import java.util.Set;

public class ANewMaximizingProblem {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out
                .println(distinctPalinsCount("abbb"));
    }
    
    //DP Approach
    // http://articles.leetcode.com/2011/11/longest-palindromic-substring-part-i.html
    public static int distinctPalinsCount(String input) {
        if(input == null || input.length() == 0) 
            return 0;
		if (input.length() <= 2)
            return input.length();
        char[] array = input.toCharArray();
        int n = input.length();
        Set<String> palins = new HashSet<String>();
        boolean[][] palin = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            // All chars of length 1 are palindrome
            palin[i][i] = true;
            palins.add(input.substring(i,i+1));
        }
        for (int i = 0; i < n - 1; i++) {
            // For length 2 strings if char[0] != char[1] then it is not a palindrome
            if (array[i] == array[i + 1]) {
                palin[i][i + 1] = true;
				palins.add(input.substring(i, i + 2));
            }
        }
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1;
                if (array[i] == array[j] && palin[i + 1][j - 1]) {
                    palin[i][j] = true;
                    palins.add(input.substring(i,j+1));
                }
            }
        }
        System.out.println(palins);
//        for (int i = 0; i < palin.length; i++)
//            System.out.println(Arrays.toString(palin[i]));
        return palins.size();
    }
    
    private static String longestPalinWithoutSpace(String input) {
        if(input == null || input.length() == 0) 
            return "";
        if(input.length() ==1)
            return input;
        if(input.length() == 2) {
            if(input.charAt(0) == input.charAt(1))
                return input;
        }
        String max = input.substring(0, 1);
        char[] array = input.toCharArray();
        for(int i=0;i<array.length;i++) {
            String temp = expand(array,i,i);
            // This is odd length palindromes.
            if(temp.length() > max.length())
                max = temp;
            // This is even length palindromes.
            temp = expand(array, i, i+1);
            if(temp.length() > max.length())
                max = temp;
        }
        return max;
    }

    private static String expand(char[] array, int c1, int c2) {
        int l = c1;
        int r = c2;
        while(l>=0 && r<array.length && array[l] == array[r]) {
            l--;r++;
        }
        return new String(array,l+1, r-1-(l+1)+1);
    }
}

