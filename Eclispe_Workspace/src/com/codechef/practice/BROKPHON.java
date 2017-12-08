package com.codechef.practice;

import java.util.Scanner;

public class BROKPHON {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numOfTests = scanner.nextInt();
        for (int i = 0; i < numOfTests; i++) {
            int numOfNums = scanner.nextInt();
            int num1 = scanner.nextInt();
            int num2;
            int wrongs = 0;
            boolean isDoubt = false;
            for (int j = 1; j < numOfNums; j++) {
                num2 = scanner.nextInt();
                if (num2 != num1) {
                    wrongs++;
                    isDoubt = true;
                } else if (isDoubt) {
                    wrongs++;
                    isDoubt = false;
                }
                num1 = num2;
            }
            if (isDoubt)
                wrongs++;
            System.out.println(wrongs);
        }
        scanner.close();
    }

    
}
