package com.gforg.strings;

import java.util.Arrays;


public class LongestCommonSubstring {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LongestCommonSubstring lcs = new LongestCommonSubstring();
        System.out.println(lcs.longestCommonSubstring("abc", "abcde"));
        System.out.println(lcs.longestCommonSubstring("www.lintcode.com code", "www.ninechapter.com code"));

    }
    
    public int longestCommonSubstring(String A, String B) {
        if (A == null || B == null || A.length() == 0 || B.length() == 0)
            return 0;
        int aLen = A.length();
        int bLen = B.length();
//        System.out.println("Alen="+aLen+" blen="+bLen);
        int[][] common = new int[aLen][bLen];
        int max = 0;
        for (int i = 0; i < aLen; i++) {
            common[i][0] = A.charAt(i) == B.charAt(0) ? 1 : 0;
            if (max < common[i][0])
                max = common[i][0];
        }
        for (int j = 0; j < bLen; j++) {
            common[0][j] = A.charAt(0) == B.charAt(j) ? 1 : 0;
            if (max < common[0][j])
                max = common[0][j];
        }
        for (int i = 1; i < aLen; i++) {
            for (int j = 1; j < bLen; j++) {
                if (A.charAt(i) == B.charAt(j)) {
                    common[i][j] = common[i - 1][j - 1] + 1;
                    if (max < common[i][j])
                        max = common[i][j];
                }
            }
        }
//        for (int i = 0; i < aLen; i++) {
//            System.out.println(Arrays.toString(common[i]));
//        }
        return max;
    }

}
