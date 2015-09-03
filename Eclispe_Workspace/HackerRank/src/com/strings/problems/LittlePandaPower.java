package com.strings.problems;

import java.util.Scanner;

public class LittlePandaPower {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            long num = scanner.nextInt();
            long pow = scanner.nextLong();
            long mod = scanner.nextLong();
            if(pow < 0){
                num = getModularInverse(num, mod);
            }
            long res = calculateModPower(num, Math.abs(pow), mod);
            System.out.println(res);
        }
        scanner.close();
    }

    private static long getModularInverse(long num, long mod) {
        long euler = getEulerValue(mod) - 1;
        return calculateModPower(num, euler, mod);
    }
    
    static public long calculateModPower(long a, long b, long mod) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) == 1)
                res = (res * a) % mod;
            a = (a * a) % mod;
            b = b >> 1;
        }
        return res;
    }
    
    static public long getEulerValue(long num) {
        int i = 2;
        long res = num;
        for (; i * i <= num; i++) {
            if (num % i == 0) {
                res = res / i;
                res = res * (i - 1);
            }
            while (num % i == 0) {
                num = num / i;
            }
        }
        if (num > 1) {
            res = res / num;
            res = res * (num - 1);
        }
        return res;
    }
}
