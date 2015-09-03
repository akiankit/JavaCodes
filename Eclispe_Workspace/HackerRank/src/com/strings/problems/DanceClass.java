package com.strings.problems;

import java.util.Scanner;

public class DanceClass {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for(int i=1;i<100;i++){
            System.out.println(i+"="+getAnswer(i));
        }
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            long n = scanner.nextLong();
            long res = getAnswer(n);
            System.out.println(res);
            if((res&1) ==1){
                System.out.println("odd");
            }else{
                System.out.println("even");
            }
        }
        scanner.close();
    }

    private static long getAnswer(long num) {
        long count = 0;
        if(num > 10) {
            int i = 1;
            for (; i <= num/2; i++) {
                if (((num / i) & 1) == 1)
                    count++;
            }
            count = count+ (num-i)+1;
        }else{
            for (int i = 1; i <= num; i++) {
                if (((num / i) & 1) == 1)
                    count++;
            }
        }
        return count;
        
    }
    
}
