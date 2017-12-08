package com.hackerearth.challenges.week17;

import java.io.IOException;
import java.util.Scanner;

public class PowerofFive {
	
	public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);
        int res;
        String _S;
        try {
            _S = in.next();
        } catch (Exception e) {
            _S = null;
        }
        
        res = getmin(_S);
        System.out.println(res);
        in.close();
    }

	static int getmin(String S) {
        int res = powOfFive(S, 0);
        if (res == 50)
            return -1;
        return res;
    }

    private static int powOfFive(String s, int start) {
        if (start == s.length())
            return 0;
        if (s.charAt(start)=='0')
            return 50;
        //Double temp = new Double(s.substring(start));
        //if(isPowOfFive(temp))
        //    return 1;
        long num = 0;
        int count = 50;
        for (int i = start; i < s.length(); i++) {
            num = (num << 1) + s.charAt(i) - '0';
            if (isPowOfFive(num))
                count = Math.min(count, 1 + powOfFive(s, i + 1));
        }

        return count;
    }

    private static boolean isPowOfFive(double num) {
        if(num==1)
            return true;
        if (num < 5)
            return false;

        while (num >= 5) {
            if (num % 5 != 0)
                return false;

            num /= 5;
        }
        if(num==1)
        	return true;
        return false;
    }
}
