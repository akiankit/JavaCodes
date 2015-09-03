
package com.leetcode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LetterCombinationsofaPhoneNumber {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<String> letterCombinations = letterCombinations("");
        System.out.println(letterCombinations.size());
        System.out.println(letterCombinations);
    }

    public static List<String> letterCombinations(String digits) {
        List<String> result = new LinkedList<String>();
        if(digits.length() == 0){
            result.add("");
            return result;
        }
        char[][] numberArray = {
                {

                }, {

                }, {
                        'a', 'b', 'c'
                }, {
                        'd', 'e', 'f'
                }, {
                        'g', 'h', 'i'
                }, {
                        'j', 'k', 'l'
                }, {
                        'm', 'n', 'o'
                }, {
                        'p', 'q', 'r', 's'
                }, {
                        't', 'u', 'v'
                }, {
                        'w', 'x', 'y', 'z'
                }
        };
        char[] charArray = digits.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int index = charArray[i] - 48;
            if(index == 0 || index == 1)
                continue;
            char[] letters = numberArray[index];
            List<String> temp = new LinkedList<String>();
            for (int j = 0; j < letters.length; j++) {
                String curr = "" + numberArray[index][j];
                if (result.size() == 0) {
                    temp.add(curr);
                } else {
                    for (String combination : result) {
                        String toAdd = combination + curr;
                        temp.add(toAdd);
                    }
                }
            }
            // System.out.println("temp="+temp);
            result.clear();
            result = temp;
        }
        Collections.sort(result);
        return result;
    }
}
