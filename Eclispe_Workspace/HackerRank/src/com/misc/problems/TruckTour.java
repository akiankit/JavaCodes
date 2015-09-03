package com.misc.problems;

import java.util.Scanner;

public class TruckTour {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        long[] petrol = new long[count];
        long[] distance = new long[count];
        int[] possible = new int[count];
        int k = 0;
        for (int j = 0; j < count; j++) {
            petrol[j] = scanner.nextInt();
            distance[j] = scanner.nextInt();
            if (petrol[j] > distance[j]) {
                possible[k] = j;
                k++;
            }
        }
        System.out.println(getStartIndex(distance, petrol, possible, 0, k-1));
        scanner.close();
    }
    
    private static int getStartIndex(long[] distance, long[] petrols, int possible[], int start,
            int end) {
        for (int i = start; i <= end; i++) {
            if (isPossible(distance, petrols, possible[i]))
                return possible[i];
        }
        return 0;
    }
    
    private static boolean isPossible(long[] distance, long[] petrols, int startIndex) {
        long petrol = 0;
        for (int k = startIndex; k < startIndex + distance.length; k++) {
            petrol = petrol + petrols[k % distance.length];
            if (petrol < distance[k % distance.length])
                return false;
            else
                petrol = petrol - distance[k % distance.length];
        }
        return true;
    }

}