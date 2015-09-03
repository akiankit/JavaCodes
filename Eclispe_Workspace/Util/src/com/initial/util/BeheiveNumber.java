package com.initial.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BeheiveNumber {

    private static int binarySearch(long al[], int i, int j, long l) {
        int k = i;
        for (int i1 = j - 1; k <= i1;) {
            int j1 = k + i1 >>> 1;
            long l1 = al[j1];
            if (l1 < l) {
                k = j1 + 1;
            } else if (l1 > l) {
                i1 = j1 - 1;
            } else
                return j1;
        }

        return -1;
    }

    public static void main(String[] args) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            long number = 1l;
            long[] validNumbers = new long[18527];
            int i=0;
            for(; number <=1000000000;i++){
                validNumbers[i] = number;
                System.out.print(number+",");
                number = number + 6*(i+1);
            }
            int lastIndex = i;
            // System.out.println("last Index = "+lastIndex);
            String line = bufferRead.readLine().trim();
            number = Long.parseLong(line);
            while(number != -1){
                int index = binarySearch(validNumbers, 0, lastIndex, number);
                if(index != -1){
                    System.out.println("Y");
                }else{
                    System.out.println("N");
                }
                //System.out.println(index);
                line = bufferRead.readLine().trim();
                number = Long.parseLong(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
