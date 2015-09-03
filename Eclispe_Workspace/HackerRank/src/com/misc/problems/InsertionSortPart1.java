
package com.misc.problems;

import java.util.Scanner;

//5
//2 4 6 8 3
public class InsertionSortPart1 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] num = {
                2, 4, 6, 8, 10
        };
        insertIntoSorted(num);
    }

    public static void insertIntoSorted(int[] ar) {
        Scanner scanner = new Scanner(System.in);
        int numOfInputs = scanner.nextInt();
        ar = new int[numOfInputs];
        for (int i = 0; i < numOfInputs; i++) {
            ar[i] = scanner.nextInt();
        }
        int rightMostNumber = ar[numOfInputs - 1];
        int i = ar.length - 2;
        while (i >= 0 && rightMostNumber < ar[i]) {
            ar[i + 1] = ar[i];
            print(ar);
            ar[i] = rightMostNumber;
            i--;
        }
        ar[i+1] = rightMostNumber;
        
        print(ar);
        scanner.close();
    }

    private static void print(int[] ar) {
        int i = 0;
        for (; i < ar.length - 1; i++) {
            System.out.print(ar[i]);
        }
        System.out.println(ar[i]);
    }
}
