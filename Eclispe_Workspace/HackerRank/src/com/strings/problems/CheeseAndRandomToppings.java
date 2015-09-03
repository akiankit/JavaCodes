package com.strings.problems;

import java.util.Arrays;
import java.util.Scanner;

public class CheeseAndRandomToppings {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int n = scanner.nextInt();
            int r = scanner.nextInt();
            int mod = scanner.nextInt();
            long binomial = binaomialCoefficientMod(n,r,mod);
            System.out.println(binomial);
        }
        scanner.close();
    }
    
    private static long binaomialCoefficientMod(long n, long r, long mod) {
        int length = (int)Math.min(r, n - r);
        long[] upper = new long[length];
        long[] bottom = new long[length];
        long eulerValue = getEulerValue(mod);
        System.out.println("Mod="+mod+" eulerValue="+eulerValue);
        for (int i = 0; i < length; i++) {
            bottom[i] = calculateModPower(i + 1, eulerValue - 1, mod);
            upper[i] = n - (length - (i + 1));
        }
        System.out.println(Arrays.toString(bottom));
        System.out.println(Arrays.toString(upper));
        long res = 1;
        for (int i = 0; i < length; i++) {
            res = (res * bottom[i]) % mod;
        }
        for (int i = 0; i < length; i++) {
            res = (res * upper[i]) % mod;
        }
        return res;
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
