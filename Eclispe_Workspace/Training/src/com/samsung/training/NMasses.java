package com.samsung.training;

import java.util.Scanner;

public class NMasses {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            String input = scanner.next();
            System.out.println(getMass(input));
        }
        scanner.close(); 
    }

    private static int getMass(String input) {
        IntStack stack = new IntStack(input.length());
        for(int i=0;i<input.length();i++) {
            char character = input.charAt(i);
            if(character=='C'){
                stack.push(12);
            }else if(character=='H') {
                stack.push(1);
            }else if(character=='O') {
                stack.push(16);
            }else if(character == '(') {
                stack.push(-1);
            }else if(character >='2' && character <='9'){
                int temp = stack.pop();
                stack.push(temp*(character-'0'));
            }else if(character == ')') {
                int temp = stack.pop();
                int sum = 0;
                while(temp != -1){
                    sum = sum + temp;
                    temp = stack.pop();
                }
                stack.push(sum);
            }
        }
        int mass = 0;
        while(!stack.isEmpty()) {
            mass = mass + stack.pop();
        }
        return mass;
    }

}
