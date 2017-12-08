package com.hackerrank.strings.problems;

import java.util.Scanner;

public class PowerOfLargeNumbers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            char[] charA = scanner.next().toCharArray();
            char[] charB = scanner.next().toCharArray();
            int mod = 1000000007;
            long a = getBigMod(mod, charA);
            long b = getBigMod(mod-1, charB);
//            System.out.println(a);
//            System.out.println(b);
            System.out.println(calculateModPower(a, b, mod));
        }
        scanner.close();
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
    
    static public long getBigMod(long mod, char[] input) {
        long res = 0;
        for (int i = 0; i < input.length; i++) {
            res = (res * 10 + (input[i] - '0')) % mod;
        }
        return res;
    }
    
    

}
