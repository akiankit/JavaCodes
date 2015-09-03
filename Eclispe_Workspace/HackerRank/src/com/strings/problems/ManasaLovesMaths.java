package com.strings.problems;

import java.util.Scanner;

public class ManasaLovesMaths {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            char[] input = scanner.next().toCharArray();
            if (input.length >= 3) {
                boolean isDivisible = isPermutationDivisibleByEight(input);
                if(isDivisible)
                    System.out.println("YES");
                else
                    System.out.println("NO");
            } else if (input.length == 1) {
                int num = input[0] - '0';
                if (num % 8 == 0)
                    System.out.println("YES");
                else
                    System.out.println("NO");
            } else if (input.length == 2) {
                int a1 = input[0] - '0';
                int a2 = input[1] - '0';
                if (((a1 * 10 + a2) % 8 == 0) || ((a2 * 10 + a1) % 8 == 0))
                    System.out.println("YES");
                else
                    System.out.println("NO");
            }
        }
        scanner.close();
    }
    
    private static boolean isPermutationDivisibleByEight(char[] input) {
        for (int k = 0; k < input.length - 2; k++) {
            int a1 = input[k] - '0';
            for (int l = k + 1; l < input.length - 1; l++) {
                int a2 = input[l] - '0';
                for (int m = l + 1; m < input.length; m++) {
                    int a3 = input[m] - '0';
                    if (isDivisibleByEight(a1, a2, a3))
                        return true;
                }
            }
        }
        return false;
    }

    private static boolean isDivisibleByEight(int a1, int a2, int a3) {
        int num = a1 * 100 + a2 * 10 + a3;
        if (num % 8 == 0)
            return true;
        num = a1 * 100 + a3 * 10 + a2;
        if (num % 8 == 0)
            return true;
        num = a2 * 100 + a3 * 10 + a1;
        if (num % 8 == 0)
            return true;
        num = a2 * 100 + a1 * 10 + a3;
        if (num % 8 == 0)
            return true;
        num = a3 * 100 + a2 * 10 + a1;
        if (num % 8 == 0)
            return true;
        num = a3 * 100 + a1 * 10 + a2;
        if (num % 8 == 0)
            return true;
        return false;
    }
    
    

}
