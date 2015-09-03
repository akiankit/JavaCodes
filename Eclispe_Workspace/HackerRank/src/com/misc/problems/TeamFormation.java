package com.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class TeamFormation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            int count = scanner.nextInt();
//            System.out.println(count);
            int[] array = new int[count];
            for (int j = 0; j < count; j++) {
                array[j] = scanner.nextInt();
            }
//            System.out.println(Arrays.toString(array));
            if(count <=1) {
                System.out.println(count);
            }else{
                System.out.println(getSmallestTeamSize(array));
            }
        }
        scanner.close();
    }
    
    private static int getSmallestTeamSize(int[] array) {
        if (array == null)
            return 0;
        int min = Integer.MAX_VALUE;
        Arrays.sort(array);
        System.out.println(Arrays.toString(array));
        for (int i = 1; i < array.length; i++) {
            int size = 1;
            while (i < array.length && array[i] - array[i-1] == 1) {
                i++;
                size++;
            }
            if (min > size) {
                min = size;
            }
            if (i == array.length - 1 && array[i] - array[i - 1] != 1) {
                return 1;
            }
        }
        return min;
    }

}
