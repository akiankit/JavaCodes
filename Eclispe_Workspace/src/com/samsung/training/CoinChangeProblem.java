package com.samsung.training;

import java.util.Arrays;

public class CoinChangeProblem {

    public static void main(String[] args) {
        int[] denominations = {1,5,10,16};
        minNumberOfCoins(denominations, 20);
    }
    
    public static int minNumberOfCoins(int[] denominations, int sum) {
        int[] sums = new int[sum + 1];
        sums[0] = 0;
        for (int i = 1; i <= sum; i++)
            sums[i] = 100;
        // V[j]<i && M[i-V[j]]+1 <M[i] then new mininum possible value is found
        for (int i = 1; i <= sum; i++) {
            for (int j = 0; j < denominations.length; j++) {
                int currentCoin = denominations[j];
                if (currentCoin <= i) {
                    int newSum = sums[i - currentCoin] + 1;
                    if (newSum < sums[i])
                        sums[i] = newSum;
                }
            }
        }
        System.out.println(Arrays.toString(sums));
        return sums[sum];
    }

}
