
package com.hackerrank.misc.problems;

import java.util.Scanner;

public class SansaAndXOR {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numOfTestCase = scanner.nextInt();
        for (int i = 0; i < numOfTestCase; i++) {
            int numOfInputs = scanner.nextInt();
            int result = 0;
            int num[] = new int[numOfInputs];
            for (int j = 0; j < numOfInputs; j++) {
                num[j] = scanner.nextInt();
            }
            
            System.out.println(result);
        }
        scanner.close();
    }

}
