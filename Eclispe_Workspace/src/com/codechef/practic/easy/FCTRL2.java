package com.codechef.practic.easy;

import java.math.BigInteger;
import java.util.Scanner;

class FCTRL2{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        String[] facts = new String[101];
        facts[0] = facts[1]="1";
        for(int i=2;i<=100;i++){
            facts[i] = new BigInteger(facts[i - 1]).multiply(
                    new BigInteger(new Integer(i).toString())).toString();
        }
        for(int i=0;i<tests;i++) {
            int input = scanner.nextInt();
            System.out.println(facts[input]);
        }
        scanner.close();
    }
}