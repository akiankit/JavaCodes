package com.categorize.late;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Temp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<Long> primes_Less_Than_N = list_Of_Primes_Less_Than_N(1000000);
        Map<Long,Long> sumUpToN = new HashMap<Long,Long>();
        sumUpToN.put(primes_Less_Than_N.get(0), primes_Less_Than_N.get(0));
        int i=0;
        long latestSum = 2;
        try {
            for(i=1;i<primes_Less_Than_N.size();i++){
                latestSum+=primes_Less_Than_N.get(i);
                sumUpToN.put(primes_Less_Than_N.get(i), latestSum);
            }   
        } catch (Exception e) {
            System.out.println("i="+i);
        }
        /*for(int i=0;i<10;i++){
            Scanner  scanner = new Scanner(System.in);
            int nextInt = scanner.nextInt();
        }*/
        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken= "+(endTime - startTime));
    }

    
    static public List<Long> list_Of_Primes_Less_Than_N(int n) {
        List<Long> primes = new LinkedList<Long>();
        if (n >= 2) {
            primes.add(2l);
            long j = 3;
            if (n > 2) {
                for (; j <= n; j++) {
                    boolean isPrime = true;
                    long temp = primes.get(0);
                    for (int k = 1; temp <= Math.sqrt(j); k++) {
                        if (j % temp == 0) {
                            isPrime = false;
                            break;
                        }
                        temp = primes.get(k);
                    }
                    if (true == isPrime && j <= n) {
                        primes.add(j);
                    }
                    if (j > n) {
                        break;
                    }
                }
            }

        }
        return primes;
    }
}