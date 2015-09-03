package com.codechef.contests;

public class Interview {
	public static void main(String[] args) {
		Interview a = new Interview();
		for(int i = 1; i <= 10; i++) {
			System.out.println(i + ", " + (a.staresIter(i) == a.staresDP(i)) + ", " + a.staresDP(i));
		}
	}
	int staresIter(int n) {
	    if(n < 3)
	        return n;
	     int a = 1, b = 1, c = 2;
	     for(int i = 3; i <= n; i++) {
	         int temp = a + b + c;
	         a = b;
	         b = c;
	         c = temp;
	     }
	     return c;
	}

	int staresDP(int n) {
	    int[] ar = new int[n + 1];
	    return solveStares(ar, n);
	}

	int solveStares(int[] ar, int n) {
		if(n == 0)
			return 1;
	    if(n < 3)
	        return n;
	    if(ar[n] > 0)
	        return ar[n];
	    ar[n] = solveStares(ar, n - 1) + solveStares(ar, n - 2) + solveStares(ar, n - 3);
	    return ar[n];
	}
}
