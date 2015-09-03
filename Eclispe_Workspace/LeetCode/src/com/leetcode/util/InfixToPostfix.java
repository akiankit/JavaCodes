package com.leetcode.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// http://cse.iitkgp.ac.in/~debdeep/teaching/FOCS/slides/TreesnRelations.pdf
public class InfixToPostfix {

    public static void main(String[] args) {
        InfixToPostfix inToPost = new InfixToPostfix();
        String postfix = inToPost.convertInfixToPostfix("3+2*2");
        System.out.println(postfix);
        System.out.println(inToPost.evaluatePostfixExpression(postfix));
        postfix = inToPost.convertInfixToPostfix(" 3/2 ");
        System.out.println(postfix);
        System.out.println(inToPost.evaluatePostfixExpression(postfix));
        postfix = inToPost.convertInfixToPostfix(" 3+5 / 2 ");
        System.out.println(postfix);
        System.out.println(inToPost.evaluatePostfixExpression(postfix));
    }
    
    public String convertInfixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder(infix.length());
        Stack<Character> stack = new Stack<Character>();
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('+', 1);
        map.put('-', 1);
        map.put('*', 2);
        map.put('/', 2);
        map.put('(', 10);
        int count = 0;
        for (int i = 0; i < infix.length(); i++) {
            char character = infix.charAt(i);
            if (character == ' ' || character == '\t')
                continue;
            else if (character >= '0' && character <= '9') {
                // This is required for integers more than one digits. If we will put every number in () then it will not be a problem.
                // For clearification run the programe and print postfix expression with and without parantheses.
                if (count == 0) {
                    count = 1;
                    postfix.append('(');
                }
                postfix.append(character);
            } else if (character == ')') {
                if (count == 1) {
                    postfix.append(')');
                    count = 0;
                }
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(')
                    stack.pop();
            } else if (character == '(') {
                stack.push(character);
            } else {
                if (count == 1) {
                    postfix.append(')');
                    count = 0;
                }
                while (!stack.isEmpty() && map.get(character) <= map.get(stack.peek())
                        && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.push(character);
            }
        }
        if (count == 1) {
            postfix.append(')');
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }
    
    public int evaluatePostfixExpression(String postfix) {
        Stack<Integer> stack = new Stack<Integer>();
        for(int i=0;i<postfix.length();i++) {
            char character = postfix.charAt(i);
            if(character == '(') {
                int num = 0;
                i++;
                while(postfix.charAt(i) != ')') {
                    num = num * 10 + postfix.charAt(i) - '0';
                    i++;
                }
                stack.push(num);
            }else {
                int num2 = stack.pop();
                int num1 = stack.pop();
                int res = 0;
                switch (character) {
                    case '+':
                        res = num1 + num2;
                        break;
                    case '-':
                        res = num1 - num2;
                        break;
                    case '*':
                        res = num1 * num2;
                        break;
                    case '/':
                        res = num1 / num2;
                        break;
                    default:
                        break;
                }
                stack.push(res);
            }
        }
        return stack.pop();
    }

}
