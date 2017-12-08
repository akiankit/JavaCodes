package com.samsung.training;

import java.util.Scanner;

public class Parantheses {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            scanner.nextInt();
            String input = scanner.next();
            System.out.println(isValid(input));
        }
        scanner.close(); 
    }

    private static int isValid(String input) {
        CharStack stack = new CharStack(input.length());
        for(int i=0;i<input.length();i++) {
            char character = input.charAt(i);
            if(character == '(' || character == '{' || character == '[' || character == '<'){
                stack.push(character);
            }else{
                char top = stack.pop();
                if(character == ')' && top != '(')
                    return 0;
                else if(character == '}' && top != '{')
                    return 0;
                else if(character == ']' && top != '[')
                    return 0;
                else if(character == '>' && top != '<')
                    return 0;
            }
        }
        return 1;
    }
}
