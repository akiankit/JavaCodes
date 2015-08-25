package com.learning.geeksforgeeks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//https://leetcode.com/problems/minimum-window-substring/
public class SmallestWindowContainingAllChars {

    static Map<Character, Integer> map = new HashMap<Character, Integer>();
    static Map<Character, Integer> tempMap = new HashMap<Character, Integer>();
    public static void main(String[] args) {
    	//These are some sample test cases for which program is giving correct output.
//        System.out.println(smallestWindow("cabeca".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("cfabeca".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("cabefgecdaecf".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("cabwefgewcwaefcf".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("abcabdebac".toCharArray(), "cda".toCharArray()));
//        System.out.println(smallestWindow("abcabdebac".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("acbdbaab".toCharArray(), "aabd".toCharArray()));
//        System.out.println(smallestWindow("caaec".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("caae".toCharArray(), "cae".toCharArray()));
//        System.out.println(smallestWindow("acbbaab".toCharArray(), "aab".toCharArray()));
//        System.out.println(smallestWindow("acbba".toCharArray(), "aab".toCharArray()));
//        System.out.println(smallestWindow("adobecodebanc".toCharArray(), "abc".toCharArray()));
//        System.out.println(smallestWindow("adobecodebanc".toCharArray(), "abcda".toCharArray()));
//        System.out.println(smallestWindow("adobecodebanc".toCharArray(), "abdbac".toCharArray()));
//        System.out.println(smallestWindow("adobecodebancbbcaa".toCharArray(), "abc".toCharArray()));
//        System.out.println(smallestWindow("aaaaaaaaaaaaaaa".toCharArray(), "a".toCharArray()));
//        System.out.println(smallestWindow("aaaaaaaaaaaaaaa".toCharArray(), "aaaaaaaaaaaaaa".toCharArray()));
//        System.out.println(smallestWindow("acccabeb".toCharArray(), "ab".toCharArray()));
//        System.out.println(smallestWindow("aaabdacefaecbef".toCharArray(), "abc".toCharArray()));
//        System.out.println(smallestWindow("coobdafceeaxab".toCharArray(), "abc".toCharArray()));
//        System.out.println(smallestWindow("of_characters_and_as".toCharArray(), "aas".toCharArray()));
//        System.out.println(smallestWindow("a".toCharArray(), "a".toCharArray()));
//        System.out.println(smallestWindow("a".toCharArray(), "b".toCharArray()));
//        System.out.println(smallestWindow("aa".toCharArray(), "a".toCharArray()));
//        System.out.println(smallestWindow("aaa".toCharArray(), "aaa".toCharArray()));
        System.out.println(smallestWindow("ab".toCharArray(), "ab".toCharArray()));
    	System.out.println(smallestWindow("babb".toCharArray(), "baba".toCharArray()));
        
    }

    public String minWindow(String s, String t) {
		if (s.length() < t.length())
    		return "";
    	return smallestWindow(s.toCharArray(), t.toCharArray());
    }

    private static String smallestWindow(char[] txt, char[] pat) {
        //step 1
        createCountMap(pat);
        int finalStart = 0;
        int start = 0;
        int end = 0;
        int minWindow = txt.length;
        int tempWindow = 0;
        // step 2
        int[] window = new int[2];
        boolean ispossible = findFirstWindow(txt,window);
        if(!ispossible)
        	return "";
		finalStart = start = window[0];
        end = window[1];
        minWindow = end-start;
//        if(end == txt.length)
//        	return "";
//        System.out.println(new String(txt,start,minWindow));
        while(end < txt.length) {
            if(txt[end] == txt[start]) {
                int tempStart = removeLeftMostAndOtherExtraChars(txt,start,end);
                tempWindow = end - tempStart+1;
                //System.out.println(new String(txt,tempStart,tempWindow));
                if(minWindow > tempWindow) {
                    finalStart = tempStart;
                    minWindow = tempWindow;
                }
                start = tempStart;
            }
            if (tempMap.containsKey(txt[end])) {
                tempMap.put(txt[end], tempMap.get(txt[end]) + 1);
            } else if(map.containsKey(txt[end])){
                tempMap.put(txt[end], 1);
            }
            end = end +1;
        }
        return new String(txt,finalStart,minWindow);
    }

    private static int removeLeftMostAndOtherExtraChars(char[] txt, int start, int end) {
    	if(tempMap.get(txt[start]) != null)
    		tempMap.put(txt[start], tempMap.get(txt[start]) - 1);
        start++;
        while (start < end) {
            if (map.get(txt[start]) == null){
                start++;
                continue;
            }
            else {
                if (tempMap.get(txt[start]) > map.get(txt[start])) {
                    tempMap.put(txt[start], tempMap.get(txt[start]) - 1);
                } else if (tempMap.get(txt[start]) <= map.get(txt[start])) {
                    break;
                }
                start++;
            }
            
        }
        return start;
    }

    private static boolean findFirstWindow(char[] txt,int[] res) {
    	tempMap.clear();
        int start=0,i = 0;
        boolean startAssigned = false;
        boolean isPossible = false;
        while (!check(tempMap)) {
            if (tempMap.containsKey(txt[i])) {
                tempMap.put(txt[i], tempMap.get(txt[i]) + 1);
            } else if(map.containsKey(txt[i])){
            	if(!startAssigned) {start = i;startAssigned = true;}
                tempMap.put(txt[i], 1);
            }
            i++;
            if(i == txt.length)
                break;
        }
        isPossible = check(tempMap);
        if (isPossible) {
	        while(start < i){
	        	if (tempMap.containsKey(txt[start])) {
	        		if(tempMap.get(txt[start]) == map.get(txt[start])){
	        			break;
	        			}
	        		else
	        			tempMap.put(txt[start], tempMap.get(txt[start]) - 1);
	            }
	        	start++;
	        }
        }
        res[0] = start;
        res[1] = i;
        return isPossible;
    }

    private static boolean check(Map<Character, Integer> tempMap) {
        Set<Character> keySet = map.keySet();
        for (Character character : keySet) {
            if(tempMap.get(character) == null  || tempMap.get(character) < map.get(character))
                return false;
        }
        return true;
    }

    private static void createCountMap(char[] pat) {
    	map.clear();
        for (int i = 0; i < pat.length; i++) {
            if (map.containsKey(pat[i])) {
                map.put(pat[i], map.get(pat[i]) + 1);
            } else {
                map.put(pat[i], 1);
            }
        }
    }
}
