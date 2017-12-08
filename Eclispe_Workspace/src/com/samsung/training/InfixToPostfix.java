package com.samsung.training;

import java.util.Scanner;

public class InfixToPostfix {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            String infix = scanner.next();
            InfixToPostfix infixToPostfix = new InfixToPostfix();
            String postfix = infixToPostfix.convertInfixToPostfix(infix.toCharArray());
            System.out.println(infixToPostfix.evaluatePostfix(postfix));
        }
        scanner.close();
    }

    public String convertInfixToPostfix(char[] infix) {
        StringBuilder postfix = new StringBuilder(infix.length);
        CharStack stack = new CharStack(infix.length);
        for (int i = 0; i < infix.length; i++) {
            char character = infix[i];
            if (character >= '0' && character <= '9') {
                postfix.append(character);
            } else if (character == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(')
                    stack.pop();
            } else if (character == '(') {
                stack.push(character);
            } else {
                while (!stack.isEmpty()
                        && getPriorityOfOperator(character) <= getPriorityOfOperator(stack.peek())
                        && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.push(character);
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }
    
    public int getPriorityOfOperator(char operator) {
        int priority = 0;
        switch (operator) {
            case '+':
            case '-':
                priority = 1;
                break;
            case '*':
            case '/':
                priority = 2;
                break;
            case '(':
                priority = 10;
                break;
            default:
                break;
        }
        return priority;
    }
    
    public int evaluatePostfix(String postfix) {
        IntStack stack = new IntStack(postfix.length());
        for (int i = 0; i < postfix.length(); i++) {
            char character = postfix.charAt(i);
            if (character >= '0' && character <= '9') {
                stack.push(character-'0');
            } else {
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

class CharStack{
    char[] arr;
    int top;
    
    public boolean isEmpty() {
        return top == -1;
    }

    public CharStack(int length) {
        arr = new char[length];
        top = -1;
    }
    
    public void push(char val){
        top++;
        arr[top] = val;
    }
    
    public char pop() {
        char temp =arr[top];
        top--;
        return temp;
    }
    
    public char peek() {
        return arr[top];
    }
}

class IntStack{
    int[] arr;
    int top;
    
    public boolean isEmpty() {
        return top == -1;
    }

    public IntStack(int length) {
        arr = new int[length];
        top = -1;
    }
    
    public void push(int val){
        top++;
        arr[top] = val;
    }
    
    public int pop() {
        int temp =arr[top];
        top--;
        return temp;
    }
    
    public int peek() {
        return arr[top];
    }
}
