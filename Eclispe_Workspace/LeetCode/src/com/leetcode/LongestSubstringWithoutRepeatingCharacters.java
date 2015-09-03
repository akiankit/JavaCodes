package com.leetcode;

public class LongestSubstringWithoutRepeatingCharacters {

    public static void main(String[] args) {
        String test = "";
        String substring = lengthOfLongestSubstring(test);
        System.out.println(substring + " length=" + substring.length());
    }

    public static String lengthOfLongestSubstring(String s) {
        String max = "";
        String tempMax = max;
        for (int i = 0; i < s.length(); i++) {
            char[] temp = new char[1];
            temp[0] = s.charAt(i);
            String tempString = new String(temp);
            if (tempMax.contains(tempString)) {
                int index = tempMax.indexOf(tempString);
                tempMax = tempMax.substring(index + 1) + tempString;
            } else {
                tempMax = tempMax + s.charAt(i);
            }
            if (tempMax.length() > max.length()) {
                max = tempMax;
            }
        }
        return max;
    }
}
