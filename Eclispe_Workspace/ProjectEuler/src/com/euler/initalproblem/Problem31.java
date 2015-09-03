/*In England the currency is made up of pound, £, and pence, p, and there are eight coins in general circulation:

1p, 2p, 5p, 10p, 20p, 50p, £1 (100p) and £2 (200p).
It is possible to make £2 in the following way:

1×100p + 1×50p + 2×20p + 1×5p + 1×2p + 3×1p
How many different ways can £2 be made using any number of coins?*/

package com.euler.initalproblem;

public class Problem31 {

	static int total = 19;
	static int[] coins = { 1, 2, 5, 10, 20, 50, 100, 200 };
	static int[] minCoinsCount = new int[total + 1];
	static int[] sumCoins = new int[total + 1];

	public static void main(String[] args) {
		change(coins, total);
		makeChange(sumCoins, coins, total);
		for (int i = 1; i < minCoinsCount.length-1; i++) {
			//System.out.println("Amount=" + (i + 1) + " Min number of coins=" + minCoinsCount[i + 1]);
		}
		for (int i = 0; i < sumCoins.length; i++) {
			//System.out.println("Amount="+(i+1)+" First Coin="+sumCoins[i+1]);
		}
	}

	static void change(int[] coins, int total) {
		for (int i = 1; i <= total; i++) {
			int min = total + 1;
			int coin = 0;
			for (int j = 0; j < coins.length; j++) {
				if (coins[j] <= i) {
					if (1 + minCoinsCount[i - coins[j]] < min) {
						min = 1 + minCoinsCount[i - coins[j]];
						coin = coins[j];
						sumCoins[i] = coin;
					}
				}
			}
			minCoinsCount[i] = min;
		}
	}
	
	static void makeChange(int[] sum,int[] coins, int total){
		while(total > 0){
			System.out.println(sum[total]);
			total = total - sumCoins[total];
		}
	}

}
