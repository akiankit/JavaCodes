package com.strings.problems;

import java.util.Scanner;

public class FlippingBits {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long l = makeRequiredNumber();
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            long n = scanner.nextLong();
//            System.out.println(Long.toBinaryString(n));
//            System.out.println(Long.toBinaryString(~n));
//            System.out.println(Long.toBinaryString(l));
//            System.out.println(Long.toBinaryString(l & (~n)));
            long res = (l & (~n));
            System.out.println(res);
        }
        scanner.close();
    }

    private static long makeRequiredNumber() {
        char[] binary = new char[64];
        for(int i=0;i<=31;i++){
            binary[i] = '0';
        }
        for(int i=32;i<64;i++) {
            binary[i] = '1';
        }
        String string = new String(binary);
//        System.out.println(string);
        long res = Long.parseLong(string,2);
//        System.out.println(res);
        return res;
    }
}
