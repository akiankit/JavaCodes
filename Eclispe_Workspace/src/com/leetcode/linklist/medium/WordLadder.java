package com.leetcode.linklist.medium;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class WordLadder {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Set<String> dict = new HashSet<String>();
        dict.add("hot");
        dict.add("dot");
        dict.add("dog");
        dict.add("lot");
        dict.add("log");
        String start = "hit";
        String end = "cog";
        System.out.println(ladderLength(start, end, dict));
    }
    
    public static int ladderLength(String start, String end, Set<String> dict) {
        LinkedList<String> queue = new LinkedList<String>();
        queue.add(start);
        dict.add(end);
        int step = 0;
        while (!queue.isEmpty()) {
            System.out.println(queue);
            LinkedList<String> level = new LinkedList<String>();
            step++;
            while (!queue.isEmpty()) {
                String q = queue.pollFirst();
                if (q.equals(end)){
                    return step;
                }
                for (int i = 0; i < start.length(); i++) {
                    for (char c = 'a'; c <= 'z'; c++) {
                        String s = q.substring(0, i) + c + q.substring(i + 1, start.length());
                        System.out.println(s);
                        if (dict.contains(s)) {
                            System.out.println("added");
                            level.add(s);
                            dict.remove(s);
                        }
                    }
                }
            }
            queue = level;
        }
        return 0;
    }

}
