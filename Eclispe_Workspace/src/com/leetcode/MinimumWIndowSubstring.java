
package com.leetcode;

public class MinimumWIndowSubstring {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static String minWindow(String S, String T) {
        char[] charArray = S.toCharArray();
        int resultWindow = 0;
        int[] countArray = new int[256];
        char[] Tarray = T.toCharArray();
        for (int i = 0; i < Tarray.length; i++) {
            countArray[i] = countArray[i] + 1;
        }
        int numOfChars = Tarray.length;
        char[] sArray = S.toCharArray();
        int start = 0;
        int end = 0;
        int hits = 0;
        int[] tempArray = new int[256];
        for (int i = 0; i < sArray.length; i++) {
            end++;
            if (countArray[i] > 0){
                tempArray[i] = tempArray[i] + 1;
                hits++;
            }
            if(hits == numOfChars){
                
            }
        }
        return null;
    }
}
