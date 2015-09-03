package com.samsung.training;

import java.util.Arrays;

public class PrimesLessThanN {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        /*int n = (int)Math.pow(10, 8);
        System.out.println(gePrimesLessThanNSieve(n));
        long end = System.currentTimeMillis();
        System.out.println("Time taken=" + (end - start));
        
        start = System.currentTimeMillis();
        System.out.println(gePrimesLessThanN(n));
        end = System.currentTimeMillis();
        System.out.println("Time taken=" + (end - start));*/
        
//        System.out.println(getGCdEuclid(30, 20));
//        System.out.println(getBigMod(11, "257"));
        /*int numbers[] = new int[100];
        int factors[] = new int[100];
        getPrimeFactors(100, numbers, factors);
        System.out.println(Arrays.toString(numbers));
        System.out.println(Arrays.toString(factors));*/
//        System.out.println(totalNumberOfFactors(13));
        System.out.println(getEulerValue(6));
    }
    
    static public long getGCdEuclid(long a, long b) {
        if (b == 0)
            return a;
        return getGCdEuclid(b, a % b);
    }
    
    static public int getBigMod(int a, String b) {
        int res = 0;
        for (int i = 0; i < b.length(); i++) {
            res = (res * 10 + (b.charAt(i) - '0')) % a;
        }
        return res;
    }
    
    static public int totalNumberOfFactors(int num) {
        int numbers[] = new int[100];
        int factors[] = new int[100];
        getPrimeFactors(num, numbers, factors);
        int res = factors[0] + 1;
        for(int i=1;i<factors.length && factors[i] !=0;i++){
            res *=  (factors[i] + 1);
        }
        return res;
    }
    
    static public void getPrimeFactors(int num, int numbers[], int factors[]) {
        int j = 0;
        int count = 0;
        int i = 2;
        for (; i * i <= num; i++) {
            count = 0;
            while (num % i == 0) {
                count++;
                num = num / i;
            }
            if (count != 0) {
                numbers[j] = i;
                factors[j] = count;
                j++;
            }
        }
        if(num> 1){
            numbers[j] = num;
            factors[j] = 1;
            j++;
        }
    }
    
    static public int getEulerValue(int num) {
        int i = 2;
        int res = num;
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
    
    static public int gePrimesLessThanN(int n) {
        int[] arr = new int[n+1];
        for (int i = 1; i < n + 1; i++) {
            arr[i] = 1;
        }
        for (int i = 2; i <= n; i++) {
            for(int j=i+i;j<=n;j+=i){
                arr[j] = 0;
            }
        }
        int count = 1;
        for(int i=3;i<=n;i+=2){
            if(arr[i] == 1)
                count++;
        }
        return count;
    }
    
    static public int gePrimesLessThanNSieve(int n) {
        int[] arr = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            arr[i] = 1;
        }
        for (int i = 3; i <= n; i += 2) {
            for (int j = 3 * i; j <= n; j += (i + i)) {
                arr[j] = 0;
            }
        }
        int count = 1;
        for (int i = 3; i <= n; i += 2) {
            if (arr[i] == 1)
                count++;
        }
        return count;
    }

}
