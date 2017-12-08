/*
 * gksfor
ay

g
a

qrq
acac
a
 */
package com.gforg.strings;

// http://www.geeksforgeeks.org/recursively-remove-adjacent-duplicates-given-string/
public class RemoveAllDuplicatesFromString {

    public static void main(String[] args) {
        String s = "ABCCDDDB";
        System.out.println(removeDuplicates(s, 0));
        s = "geeksforgeeg";
        System.out.println(removeDuplicates(s, 0));
        s = "azxxxzy";
        System.out.println(removeDuplicates(s, 0));
        s = "caaabbbaac";
        System.out.println(removeDuplicates(s, 0));
        s = "gghhg";
        System.out.println(removeDuplicates(s, 0));
        s = "aaaacddddcappp";
        System.out.println(removeDuplicates(s, 0));
        s = "aaaaaaaaaa";
        System.out.println(removeDuplicates(s, 0));
        s = "qpaaaaadaaaaadprq";
        System.out.println(removeDuplicates(s, 0));
        s = "acaaabbbacdddd";
        System.out.println(removeDuplicates(s, 0));
        s = "acbbcddc";
    }

    public static String removeDuplicates(String s, int startIndex) {
        if (s.length() == 1 || s.length() == 0)
            return s;
        else if (startIndex == s.length() - 1)
            return s;
        else {
            if (s.charAt(startIndex) == s.charAt(startIndex + 1)) {
                int index = startIndex;
                while (index < s.length() && s.charAt(index) == s.charAt(startIndex)) {
                    index++;
                }
                s = s.substring(0, startIndex) + s.substring(index);
                System.out.println(s);
                if (startIndex != 0) {
                    startIndex = startIndex - 1;
                }
                return removeDuplicates(s, startIndex);
            } else
                return removeDuplicates(s, startIndex + 1);
        }
    }

}
