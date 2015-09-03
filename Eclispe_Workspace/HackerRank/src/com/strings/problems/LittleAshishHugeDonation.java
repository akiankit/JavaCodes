package com.strings.problems;

import java.util.Scanner;

public class LittleAshishHugeDonation {

    /**
     * @param args
     */
    public static void main(String[] args) {
        long[] arr = new long[350000];
        arr[0] = 0;
        arr[1] = 1;
        long max = (long)Math.pow(10, 16);
        long i = 2;
        for (; i <= 350000; i++) {
            arr[(int)i] = (long)((2 * Math.pow(i, 3) + 3 * Math.pow(i, 2) + i) / 6);
            if (arr[(int)i] > max)
                break;
        }
//        System.out.println(i);
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int j = 0; j < tests; j++) {
            long x = scanner.nextLong();
//            System.out.println(x);
            int n = searchBinary(arr, 1, (int)i, x);
            System.out.println(n);

        }
        scanner.close();
    }
    
    private static int searchBinary(long[] arr, int low, int high, long x) {
        if(low > high)
            return low-1;
        else{
            int mid = (low + high) / 2;
            if(arr[mid] == x){
                return mid;
            } else if(arr[mid] < x){
                return searchBinary(arr, mid+1, high, x);
            } else if(arr[mid] > x){
                return searchBinary(arr, low, mid-1, x);
            }
        }
        return low ;
    }

}
