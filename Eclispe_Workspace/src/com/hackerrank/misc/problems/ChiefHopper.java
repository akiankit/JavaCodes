package com.hackerrank.misc.problems;

import java.util.Scanner;

public class ChiefHopper {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        long[] array = new long[count];
        long min = Integer.MAX_VALUE;
        long max = Integer.MIN_VALUE;
        for (int j = 0; j < count; j++) {
            array[j] = scanner.nextInt();
            if(array[j]< min)
                min = array[j];
            if(array[j]>max)
                max = array[j];
        }
//        System.out.println(min +" "+max);
        System.out.println(minEnergy(array, 0, max));
        scanner.close();
    }
    
    private static long minEnergy(long[] array, long min, long max) {
        long mid = (max + min) / 2;
//        System.out.println(min +" "+max+" "+mid);
//        if (mid == max && mid == min)
//            return mid;
        if (isPossible(array, mid)) {
            if(!isPossible(array, mid-1))
                return mid;
            else
                return minEnergy(array, min, mid-1);
        }else {
            return minEnergy(array, mid+1, max);
        }
    }
    
    private static boolean isPossible(long[] array, double energy) {
        if (energy < 0) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            energy = energy + energy- array[i];
            if (energy < 0) {
//                System.out.println(energy);
                return false;
            }
        }
//        System.out.println(energy);
        return true;
    }

}
