package com.leetcode.medium;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RepeatedDNASequences {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"));
    }

    // Solution apporach is correct but memory limit is problem.
    // Also I am not sure about substring method complexity. If it is of o(n) then time complexity is also more.
    public static List<String> findRepeatedDnaSequences(String s) {
        List<String> res = new LinkedList<String>();
        int ten = 10;
        if (s.length() < ten + 1)
            return res;
        else {
            Map<Character, Integer> map = new HashMap<Character, Integer>();
            map.put('A', 1);
            map.put('C', 2);
            map.put('G', 3);
            map.put('T', 4);
            Map<Long, Integer> added = new HashMap<Long, Integer>();
            Map<Long, Integer> temp = new HashMap<Long, Integer>();
            long code = 0;
            for (int i = 0; i < ten; i++) {
                code = code * 10 + map.get(s.charAt(i));
            }
            long div = 1000000000;
            temp.put(code, 1);
            for (int i = ten; i < s.length(); i++) {
                code = (code % div) * 10 + map.get(s.charAt(i));
                if (temp.containsKey(code)) {
                    if (!added.containsKey(code)) {
                        res.add(s.substring(i - 9, i + 1));
                        added.put(code, 1);
                    }
                } else {
                    temp.put(code, 1);
                }
            }
        }
        return res;
    }
}
