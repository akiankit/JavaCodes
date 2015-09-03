
package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class PRIME1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();
        for (int i = 0; i < testCases; i++) {
            long num1 = scanner.nextLong();
            long num2 = scanner.nextLong();
            List<Long> list = getPrimesLessThanNGreaterThanM(num1, num2);
            for (Long long1 : list) {
                System.out.println(long1);
            }
        }
        scanner.close();
    }

    static public List<Long> getPrimesLessThanNGreaterThanM(long M, long N) {
        if (M == 1)
            M = 2;
        List<Long> primes = new LinkedList<Long>();
        int totalNumbers = (int)(N - M + 1);
        long[] numbers = new long[totalNumbers];
        boolean[] isPrime = new boolean[totalNumbers];
        for (int i = 0; i < totalNumbers; i++) {
            isPrime[i] = true;
        }
        for (int i = 0; i < totalNumbers; i++) {
            numbers[i] = (M + i);
        }
        for (int i = 2; i <= Math.sqrt(N); i++) {
            for (int j = Math.max(2, (int)(M / i)); i * j <= N; j++) {
                if (i * j < M)
                    continue;
                else
                    isPrime[(int)((i * j) - M)] = false;
            }
        }
        for (int i = 0; i < totalNumbers; i++) {
            if (isPrime[i] == true)
                primes.add(numbers[i]);
        }
        return primes;
    }

}
