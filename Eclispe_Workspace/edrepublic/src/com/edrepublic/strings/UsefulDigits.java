package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UsefulDigits {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        long num = Long.parseLong(str);
        int count = 0;
        long copy = num;
        while (copy > 0) {
            int digit = (int)(copy % 10);
            if (digit != 0 && num % digit == 0)
                count++;
            copy = copy / 10;
        }
        System.out.println(count);
    }
}
