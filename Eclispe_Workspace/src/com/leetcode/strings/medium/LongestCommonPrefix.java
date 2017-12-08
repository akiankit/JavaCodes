package com.leetcode.strings.medium;

public class LongestCommonPrefix {/*
    
    class TSTNode {
        public char data;

        public boolean isEnd;

        public TSTNode left, middle, right;
        
        public int count;

        public TSTNode(char data) {
            this.data = data;
            this.isEnd = false;
            this.left = null;
            this.middle = null;
            this.right = null;
        }
    }

    *//**
     * @param args
     *//*
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String[] strs = {"ABCD","AB","ABC","BAD"};
        LongestCommonPrefix lcp = new LongestCommonPrefix();
        System.out.println(lcp.longestCommonPrefix(strs));
        
    }
    
    public String longestCommonPrefix(String[] strs) {
        if(strs == null || strs.length == 0)
            return "";
        if(strs.length == 1)
            return strs[0];
        TernarySearchTree tst = new TernarySearchTree();
        for (int i = 0; i < strs.length; i++) {
            tst.insert(strs[i]);
        }
        int count = 0;
        TSTNode root = tst.root;
        while (true) {
            if (root != null && root.left == null && root.right == null) {
                count++;
                if (root.isEnd == true)
                    break;
                else
                    root = root.middle;
            } else
                break;
        }
        System.out.println(count);
        return strs[0].substring(0, count);
    }

*/}
