package com.gforg.strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SmallestWindowContainingAllChars {

    static Map<Character, Integer> map = new HashMap<Character, Integer>();
    static Map<Character, Integer> tempMap = new HashMap<Character, Integer>();
    public static void main(String[] args) {
        smallestWindow("this is a test string".toCharArray(), "tist".toCharArray());
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
        end = findFirstWindow(txt);
        minWindow = end;
        System.out.println(new String(txt,start,minWindow));
        while(end < txt.length) {
            if(txt[end] == txt[start]) {
                int tempStart = removeLeftMostAndOtherExtraChars(txt,start,end);
                tempWindow = end - tempStart+1;
                System.out.println(new String(txt,tempStart,tempWindow));
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
        return null;
    }

    private static int removeLeftMostAndOtherExtraChars(char[] txt, int start, int end) {
        start = start+1;
        while (start < end) {
            if (map.get(txt[start]) == null){
                start++;
                continue;
            }
            else {
                if (tempMap.get(txt[start]) > map.get(txt[start])) {
                    tempMap.put(txt[start], tempMap.get(txt[start]) - 1);
                } else if (tempMap.get(txt[start]) == map.get(txt[start])) {
                    break;
                }
                start++;
            }
            
        }
        return start;
    }

    private static int findFirstWindow(char[] txt) {
        int i = 0;
        while (!check(tempMap)) {
            if (tempMap.containsKey(txt[i])) {
                tempMap.put(txt[i], tempMap.get(txt[i]) + 1);
            } else if(map.containsKey(txt[i])){
                tempMap.put(txt[i], 1);
            }
            i++;
            if(i == txt.length)
                break;
        }
        return i;
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
        for (int i = 0; i < pat.length; i++) {
            if (map.containsKey(pat[i])) {
                map.put(pat[i], map.get(pat[i]) + 1);
            } else {
                map.put(pat[i], 1);
            }
        }
    }
}
