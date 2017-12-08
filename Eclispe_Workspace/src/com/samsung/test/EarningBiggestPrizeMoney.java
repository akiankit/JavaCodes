package com.samsung.test;

import java.util.Scanner;

public class EarningBiggestPrizeMoney {
    
    static long max =0;
    static boolean found = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T;
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            long number = sc.nextLong();
            int swaps = sc.nextInt();
            int digitsCount = getNumOfDigits(number);
            int[] array = getDigitArray(number, digitsCount);
            max = 0;
            found = false;
            getMax(array, swaps);
            System.out.println("#" + test_case + " " + max);
        }
        sc.close();
    }
    
    private static void getMax(int[] input, int k) {
        if(found == true)
            return;
        if (k == 0) {
            long temp = 0;
            for (int i = 0; i < input.length; i++) {
                temp = temp * 10 + input[i];
            }
            if (temp > max) {
                max = temp;
                if (isSorted(input)) {
                    found = true;
                }
            }
            return;
        }
        for (int i = 0; i < input.length - 1; i++) {
            for (int j = i + 1; j < input.length; j++) {
                swap(input, i, j);
                getMax(input, k - 1);
                swap(input, i, j);
            }
        }
    }
    
    private static void swap(int[] input, int i, int j) {
        int temp = input[i];
        input[i] = input[j];
        input[j] = temp;
    }

    static public int getNumOfDigits(Long number) {
        int digitsCount = 0;
        while (number > 0) {
            number = number / 10;
            digitsCount++;
        }
        return digitsCount;
    }
    
    private static int[] getDigitArray(long number, int digitsCount) {
        int[] digits = new int[digitsCount];
        int index = 0;
        if (number == 0) {
            digits[0] = 0;
        } else {
            while (number > 0) {
                digits[index++] = (int)(number % 10);
                number = number / 10;
            }
        }
        digits = reverseAray(digits);
        return digits;
    }
    
    private static int[] reverseAray(int[] array) {
        int n = array.length;
        for (int i = 0; i < n / 2; i++) {
            int temp = array[n - i - 1];
            array[n - i - 1] = array[i];
            array[i] = temp;
        }
        return array;
    }
    
    private static boolean isSorted(int[] arr){
        for (int i = 1; i < arr.length; i++) {
            if(arr[i] < arr[i-1])
                return false;
        }
        return true;
    }
    
}
