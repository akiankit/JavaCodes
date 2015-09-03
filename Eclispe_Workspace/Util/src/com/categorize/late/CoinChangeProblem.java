//Given a set of coins find out the minimum number of coins to represent a given number.

package com.categorize.late;

import java.util.Arrays;

//http://www.youtube.com/watch?v=GafjS0FfAC0
public class CoinChangeProblem {

    public static void main(String[] args) {
        int[] coins = {2, 5, 10, 20, 50, 100, 200};
        int amount = 1;
        getMinimumNumberOfCoins(coins, amount);
        for (int i = 1; i <= 10; i++) {
            System.out.println("Amount=" + i + " Min Number of Coins="
                    + getMinimumNumberOfCoins(coins, i));
        }
    }
    // not using memoization
    // using memoization will result in lesser no. of calls.
    public static int getMinimumNumberOfCoins(int[] coins, int amount) {
        int minCoins = amount + 1;
        if (amount < 0)
            return -1;
        if (amount == 0)
            return 0;
        else {
            for (int i = 0; i < coins.length && coins[i] <= amount; i++) {
                int amount2 = amount - coins[i];
                int tempCoins = getMinimumNumberOfCoins(coins, amount2);
                if (tempCoins < minCoins && tempCoins != -1) {
                    minCoins = tempCoins;
                }
            }
        }
        if (minCoins == amount + 1)
            return -1;
        return minCoins + 1;
    }
    
    // Using memoization Bottom up approach
    public static int minNumberOfCoins(int[] denominations, int sum) {
        int[] sums = new int[sum + 1];
        sums[0] = 0;
        for (int i = 1; i <= sum; i++)
            sums[i] = 100;
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
