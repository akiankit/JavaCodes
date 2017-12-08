package com.gforg.strings;

public class RegexMatching {

    public static void main(String[] args) {
        System.out.println(isRegexMatch("g*ks", "geeks"));
        System.out.println(isRegexMatch("ge.ks*", "geeksforgeeks")); // Yes
        System.out.println(isRegexMatch("g*k", "gee"));  // No because 'k' is not in second
        System.out.println(isRegexMatch("*pqrs", "pqrst")); // No because 't' is not in first
        System.out.println(isRegexMatch("abc*bcd", "abcdhghgbcd")); // Yes
        System.out.println(isRegexMatch("abc*c.d", "abcd")); // No because second must have 2 instances of 'c'
        System.out.println(isRegexMatch("*c*d", "abcd")); // Yes
    }

    // Not have enough no. of test cases other than what is mentioned in main function
    public static boolean isRegexMatch(String regex, String input) {
        int lastStarIndex = -1;
        int i = 0, j = 0;
        for (; i < input.length() && j < regex.length();) {
            if (regex.charAt(j) == '*') {
                lastStarIndex = i;
                j++;
                while (j < regex.length() && (regex.charAt(j) == '*' || regex.charAt(j) == '.'))
                    j++;
                if (j == regex.length())
                    return true;
                while (i < input.length() && input.charAt(i) != regex.charAt(j)) {
                    i++;
                }
            } else if (regex.charAt(j) == '.' || regex.charAt(j) == input.charAt(i)) {
                i++;
                j++;
            } else if (lastStarIndex != -1 && i > lastStarIndex) {
                j = lastStarIndex;
            } else {
                return false;
            }
        }
        return (i == input.length() && j == regex.length());
    }
}
