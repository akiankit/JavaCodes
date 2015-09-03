/*You are given a string, S, and a list of words, L, that are all of the same length.
Find all starting indices of substring(s) in S that is a concatenation of each word in L exactly once and without any intervening characters.

For example, given:
S: "barfoothefoobarman"
L: ["foo", "bar"]
 * */

package com.leetcode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SubstringWithConcatenationOfAllWords {

    public static void main(String[] args) {
//        "barfoofoobarthefoobarman", ["bar","foo","the"]
        String S = "abababab";
        String[] L = {
                "ab","ab","ab"
        };
        System.out.println(findSubstringTST(S, L));
    }
    
    public static List<Integer> findSubstringTST(String s, String[] words) {
        List<Integer> list = new LinkedList<Integer>();
        int length = words.length * words[0].length();
        int wordLength = words[0].length();
        Map<String,Integer>  map = new HashMap<String,Integer>();
        for (int i = 0; i < words.length; i++) {
            if(map.containsKey(words[i])){
                map.put(words[i], map.get(words[i])+1);
            }else{
                map.put(words[i], 1);
            }
        }
        System.out.println(map);
        for (int i = 0; i <= s.length() - length; i++) {
            String temp = s.substring(i, i + length);
            int searchCount = 0;
            Map<String, Integer> tempMap = new HashMap<String, Integer>();
            for (int j = 0; j <= temp.length() - wordLength; j += wordLength) {
                String wordToSearch = temp.substring(j, j + wordLength);
                if (map.containsKey(wordToSearch)) {
                    if (tempMap.containsKey(wordToSearch)) {
                        tempMap.put(wordToSearch, tempMap.get(wordToSearch) + 1);
                    } else {
                        tempMap.put(wordToSearch, 1);
                    }
                    searchCount++;
                } else
                    break;
            }
            if (searchCount == words.length) {
                if (map.equals(tempMap)) {
                    list.add(i);
                }
            }
        }
        return list;
    }

    public static List<Integer> findSubstring(String S, String[] L) {
        List<Integer> result = new LinkedList<Integer>();
        int lengthOfL = 0;
        int[] count = new int[256];
        for (String string : L) {
            lengthOfL += string.length();
            for (int i = 0; i < string.length(); i++) {
                count[string.charAt(i)]++;
            }
        }
        System.out.println(lengthOfL);
        int startIndex = 0;
        int endIndex = lengthOfL;
        for (; endIndex <= S.length(); startIndex++, endIndex++) {
            if (count[S.charAt(endIndex - 1)] == 0) {
                startIndex = endIndex - 1;
                endIndex = startIndex + lengthOfL;
            }
            if (endIndex > S.length()) {
                break;
            }
            int[] countArray = makeCountArray(S.substring(startIndex, endIndex));
            if (areArraysEqual(count, countArray)
                    && isSubStringValid(L, S.substring(startIndex, endIndex))) {
                result.add(startIndex);
            }
        }
        return result;
    }

    private static boolean isSubStringValid(String[] l, String substring) {
        System.out.println(substring);
        for (int i = 0; i < l.length; i++) {
            if(substring.equals(l[i]))
                return true;
        }
        return false;
    }

    private static boolean areArraysEqual(int[] count, int[] countArray) {
        for (int i = 0; i < count.length; i++) {
            if (count[i] != countArray[i])
                return false;
        }
        return true;
    }

    private static int[] makeCountArray(String substring) {
        int[] count = new int[256];
        for (int i = 0; i < substring.length(); i++) {
            count[substring.charAt(i)]++;
        }
        return count;
    }
}
