package com.leetcode.easy;


public class CountPrimes {

    public static void main(String[] args) {
        System.out.println(countPrimes(500000000));
    }

    /*static public List<Long> gePrimesLessThanNGreaterThanM(long M, long N) {
        if(M==1)
            M=2;
        List<Long> primes = new LinkedList<Long>();
        int totalNumbers = (int)(N-M+1);
        long[] numbers = new long[totalNumbers];
        boolean[] isPrime = new boolean[totalNumbers];
        for(int i=0;i<totalNumbers;i++) {
            isPrime[i] = true;
        }
        for(int i=0;i<totalNumbers;i++){
            numbers[i] = (M+i);
        }
        for(int i=2;i<=Math.sqrt(N);i++){
            for(int j=Math.max(2,(int)(M/i));i*j<=N;j++) {
                if(i*j<M)
                    continue;
                else
                    isPrime[(int)((i*j)-M)] = false;
            }
        }
        for(int i=0;i<totalNumbers;i++){
            if(isPrime[i] == true)
                primes.add(numbers[i]);
        }
        return primes;
    } */
    
    public static int countPrimes(int n) {
        if(n<3) return 0;
        boolean[] primes = new boolean[n];
        int count = n-2; // 0 and 1 are not prime, 2 is the first prime
        for(int i=2;i<n;i++) {
            if(!primes[i])
                for(int j=i+i;j<n;j+=i) if(!primes[j]) { primes[j]=true; count--; }
        }
        return count;
    }
}
