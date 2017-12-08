package com.hackerrank.strings.problems;

import java.util.Scanner;

public class EulersCriterion {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int n = scanner.nextInt();
            int mod = scanner.nextInt();
            int euler = -1;
            if(mod != 2)
                euler = (int)getEulerCriteria(n, mod);
            if(n == 0 || euler == 1){
                System.out.println("YES");
            }else{
                System.out.println("NO");
            }
        }
        scanner.close();
    }
    
    private static long getEulerCriteria(int n, int mod) {
        int p = (mod - 1) / 2;
        return calculateModPower(n, p, mod);
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

}
