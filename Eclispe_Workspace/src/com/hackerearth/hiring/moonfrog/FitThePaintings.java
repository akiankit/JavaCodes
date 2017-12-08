package com.hackerearth.hiring.moonfrog;

import java.util.Scanner;

public class FitThePaintings {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int m = in.nextInt();
		int a = in.nextInt();
		int b = in.nextInt();
		int c = in.nextInt();
		int d = in.nextInt();
		String res = isPossible(n, m, a, b, c, d);
		System.out.println(res);
		in.close();
	}
	
	private static String isPossible(int n, int m, int a, int b, int c, int d){
		String res = "No";
        if (a + c <= n && Math.max(b, d) <= m) {
            res="Yes";
        }

        if (a + c <= m && Math.max(b, d) <= n) {
        	res="Yes";
        }

        if (a + d <= n && Math.max(b, c) <= m) {
        	res="Yes";
        }

        if (a + d <= m && Math.max(b, c) <= n) {
        	res="Yes";
        }

        if (b + c <= n && Math.max(a, d) <= m) {
        	res="Yes";
        }

        if (b + c <= m && Math.max(a, d) <= n) {
        	res="Yes";
        }

        if (b + d <= n && Math.max(a, c) <= m) {
        	res="Yes";
        }

        if (b + d <= m && Math.max(a, c) <= n) {
        	res="Yes";
        }
        return res;

	}
}

