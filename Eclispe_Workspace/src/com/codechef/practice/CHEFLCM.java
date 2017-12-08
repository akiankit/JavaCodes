package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class CHEFLCM {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numOfTests = scanner.nextInt();
        for (int i = 0; i < numOfTests; i++) {
            List<Long> factors = new LinkedList<Long>();
            int num = scanner.nextInt();
            factors = getAllFactors(num);
            long sum = 0;
            for (Long long1 : factors) {
                sum = sum + long1;
            }
            System.out.println(sum);
        }
        scanner.close();
    }

    public static List<Long> getAllFactors(long n) {
        List<Long> primeFactors = new LinkedList<Long>();
        primeFactors.add(1l);
        if (n != 1) {
            int sqrt = (int) Math.sqrt(n);
            for (long temp = 2; temp <= sqrt; temp++) {
                if (n % temp == 0) {
                    primeFactors.add(temp);
                }
            }
            List<Long> primeFactors2 = new LinkedList<Long>();
            for (int i = primeFactors.size() - 1; i >= 0; i--) {
                long long1 = primeFactors.get(i);
                long temp = n / long1;
                if (false == primeFactors.contains(temp)) {
                    primeFactors2.add(temp);
                }
            }
            primeFactors.addAll(primeFactors2);
        }

        return primeFactors;
    }
}
