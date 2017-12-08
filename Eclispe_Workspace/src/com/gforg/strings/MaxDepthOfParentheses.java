package com.gforg.strings;

import java.util.Stack;

// http://www.geeksforgeeks.org/find-maximum-depth-nested-parenthesis-string/
public class MaxDepthOfParentheses {

    public static void main(String[] args) {
        System.out.println(maxParanthesesDepth("(b) ((c) ()"));
    }

    private static int maxParanthesesDepth(String input) {
        int maxDepth = 0;
        int tempDepth = 0;
        if (input.length() == 0)
            return maxDepth;
        else {
            Stack<Character> stack = new Stack<Character>();
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '(') {
                    stack.push(input.charAt(i));
                    tempDepth++;
                    if (tempDepth > maxDepth)
                        maxDepth = tempDepth;
                } else if (input.charAt(i) == ')') {
                    if(stack.empty())
                        return -1;
                    Character pop = stack.pop();
                    if (pop != '(') {
                        return -1;
                    } else {
                        tempDepth--;
                    }
                } 
            }
            if(!stack.empty())
                return -1;
        }
        return maxDepth;
    }
}
