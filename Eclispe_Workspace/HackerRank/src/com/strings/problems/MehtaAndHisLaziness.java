package com.strings.problems;

import java.util.Scanner;

public class MehtaAndHisLaziness {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int n = scanner.nextInt();
            int total = totalNumberOfFactors(n);
            int even = evenSqaureDivisors(n);
            if(even != 0){
                long gcd = getGCdEuclid(total-1, even);
                System.out.println((even/gcd)+"/"+((total-1)/gcd));
            }else{
                System.out.println("0");
            }
            
        }
        scanner.close();
    }
    
    static public long getGCdEuclid(long a , long b) {
        if(b == 0)
            return a;
        return getGCdEuclid(b, a % b);
    }
    
    static private int evenSqaureDivisors(long num) {
        int count = 0;
        for(int i=2;i*i<num;i++){
            int square = i*i;
            if((square&1) ==0 && num%square ==0)
                count++;
        }
        return count;
    }
    
    static public int totalNumberOfFactors(int num) {
        int numbers[] = new int[10000];
        int factors[] = new int[10000];
        getPrimeFactors(num, numbers, factors);
        int res = factors[0] + 1;
        for (int i = 1; i < factors.length && factors[i] != 0; i++) {
            res *= (factors[i] + 1);
        }
        return res;
    }

    static public void getPrimeFactors(int num, int numbers[], int factors[]) {
        int j = 0;
        int count = 0;
        int i = 2;
        for (; i * i <= num; i++) {
            count = 0;
            while (num % i == 0) {
                count++;
                num = num / i;
            }
            if (count != 0) {
                numbers[j] = i;
                factors[j] = count;
                j++;
            }
        }
        if(num> 1){
            numbers[j] = num;
            factors[j] = 1;
            j++;
        }
    }
}
