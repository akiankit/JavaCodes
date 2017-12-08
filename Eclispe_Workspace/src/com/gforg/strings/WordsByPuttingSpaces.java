package com.gforg.strings;

import java.util.LinkedList;
import java.util.List;

// http://www.geeksforgeeks.org/print-possible-strings-can-made-placing-spaces/
public class WordsByPuttingSpaces {

    public static void main(String[] args) {
        String input = "ABCD";
        System.out.println(wordsWithSpaces(input));
    }

    private static List<String> wordsWithSpaces(String input) {
        List<String> res = new LinkedList<String>();
        if(input.length() == 0){
            res.add(" ");
            return res;
        }
        if(input.length() == 1) {
            res.add(input);
            return res;
        }
        else {
            for(int i=1;i<=input.length();i++) {
                List<String> list = wordsWithSpaces(input.substring(i));
                for (String string : list) {
                    if(string.equalsIgnoreCase(" "))
                        res.add(input.substring(0, i));
                    else
                        res.add(input.substring(0, i) + " " + string);
                }
            }
            return res;
        }
    }

}
