
package com.leetcode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WordBreak {
    
    static class combinations{
        List<String> list = null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
        // ["a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa"]
        String s = null;
        Set<String> dict = new HashSet<String>();
        dict.add("a");
        dict.add("aa");
        dict.add("aaa");
        dict.add("aaaa");
        dict.add("aaaaa");
        dict.add("aaaaaa");
        dict.add("aaaaaaa");
        dict.add("aaaaaaaa");
        dict.add("aaaaaaaaa");
        dict.add("aaaaaaaaaa");
        s = "aaaaaaa";

//        s = "ab";
//        dict.add("a");
//        dict.add("b");
        int[][] memo = new int[s.length()][s.length()];
        combinations[][] memoWords = new combinations[s.length()][s.length()];
        for(int i=0;i<s.length();i++){
            for(int j=0;j<s.length();j++){
                memoWords[i][j] = new combinations();
            }
        }
        System.out.println(wordBreak2(s, dict));
//        List<String> wordBreak2 = wordBreak2(s, dict, 0, s.length() - 1, memoWords);
//        System.out.println(wordBreak2);
//        System.out.println(memo[0][s.length()-1]);
    }

    /* A utility function to check whether a word is present in dictionary or not.*/
    private static boolean dictionaryContainsWord(String s, Set<String> dict) {
        for (String string : dict) {
            if(string.equalsIgnoreCase(s))
                return true;
        }
        return false;
    }
    
    // The parameter for dictionaryContains is str.substr(0, i)
    // str.substr(0, i) which is prefix (of input string) of
    // length 'i'. We first check whether current prefix is in
    // dictionary. Then we recursively check for remaining string
    // str.substr(i, size-i) which is suffix of length size-i
    // First I had used Simple Recursion then in that case 
    // TLE was happening. So modified to use memoization technique.
    // Now it's passing.
    // Terminating cases I have found manually by taking some inputs
    private static int wordBreak1(String original, Set<String> dict, int start, int end,
            int[][] memo) {
        if (start > end)
            return -1;
        String temp = original.substring(start, end + 1);
//        System.out.println("Looking for " + temp);
        if (memo[start][end] != 0) {
            return memo[start][end];
        }
        if (dictionaryContainsWord(temp, dict)) {
            memo[start][end] = 1;
            return 1;
        }
        if (temp.length() <= 1) {
            memo[start][end] = -1;
            return -1;
        }
        for (int i = start; i <= end; i++) {
            int one = 0;
            if (i != end)
                one = wordBreak1(original, dict, start, i, memo);
            int two = wordBreak1(original, dict, i + 1, end, memo);
            if (one == 1 && two == 1) {
                memo[start][end] = 1;
                return memo[start][end];
            }
        }
        memo[start][end] = -1;
        return memo[start][end];
    }
    
    private static List<String> wordBreak2(String original, Set<String> dict, int start, int end,
            combinations[][] memoWords) {
        List<String> list = new LinkedList<String>();
        if (start > end)
            return list;
        if (memoWords[start][end].list != null) {
            return memoWords[start][end].list;
        }
        String temp = original.substring(start, end + 1);
        // System.out.println("Looking for " + temp);
        if (dictionaryContainsWord(temp, dict)) {
            list.add(temp);
            memoWords[start][end].list = list;
        }
        if (temp.length() <= 1) {
            memoWords[start][end].list = list;
            return list;
        }
        for (int i = start; i <= end; i++) {
            List<String> one = new LinkedList<String>();
            List<String> two = new LinkedList<String>();
            if (i != end) {
                one = wordBreak2(original, dict, start, i, memoWords);
            }
            two = wordBreak2(original, dict, i + 1, end, memoWords);
            for (String string : one) {
                for (String string2 : two) {
                    String string3 = string + " " + string2;
                    if (!list.contains(string3))
                        list.add(string3);
                }
            }
        }
        memoWords[start][end].list = list;
        return memoWords[start][end].list;
    }
    
    public static List<String> wordBreak2(String s, Set<String> wordDict) {
        List<String> result = new LinkedList<String>();
        wordBreakHelper(new HashSet<Integer>(), 0,result, s, "", wordDict);
        return result;
    }

    public static boolean wordBreakHelper(HashSet<Integer> badIndex, int curIndex, List<String> result,
            String s, String curSentence, Set<String> wordDict) {
        boolean foundWord = false;
        for (int i = curIndex; i < s.length(); i++) {
            if (wordDict.contains(s.substring(curIndex, i + 1))) {
                String newSentence = curSentence + " " + s.substring(curIndex, i + 1);
                if (i == s.length() - 1) {
                    result.add(newSentence.substring(1));
                    foundWord = true;
                } else {
                    if (badIndex.contains(i + 1))
                        continue;
                    if (wordBreakHelper(badIndex, i + 1, result, s, newSentence, wordDict))
                        foundWord = true;
                }
            }
        }
        if (foundWord)
            return true;
        else {
            badIndex.add(curIndex);
            return false;
        }
    }

}
