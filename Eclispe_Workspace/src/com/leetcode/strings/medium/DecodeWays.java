package com.leetcode.strings.medium;

import java.util.Arrays;

public class DecodeWays {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        for(int i=1;i<100;i++){
//            System.out.println("i="+i+" Decodings="+numDecodings(String.valueOf(i)));
//        }
        System.out.println(numDecodings("90"));
    }

    //http://rleetcode.blogspot.in/2014/01/decode-ways-java.html
    static public int numDecodings(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        // declare ways array with two extra space, because ways[i] also affect by ways[i+2]
        int[] ways = new int[s.length() + 2];

        Arrays.fill(ways, 1);
        int i = s.length() - 1;

        ways[i] = s.charAt(i) == '0' ? 0 : 1;

        for (i = i - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') {
                // if current digit is '0', so no mater what right is, current ways should be 0;
                ways[i] = 0;
            } else {
                // if current digit is not '0', current ways should be ways[i+1]
                // because, for example s="12", i=0, ways[1]=1, then because current digit is not
                // zero, so for
                // each situation of when i=1, the current i=0 should be a valid way,
                ways[i] = ways[i + 1];

                // check is current digit with right 1 digit can be a valid situation,so in this
                // situation only s.charAt(i)=='1'||
                // s.charAt(i)=='2' and s.charAt(i+1)<='6' can be valid situation, the ways[i]
                // should + ways[i+2];

                if (i + 2 < ways.length && s.charAt(i) == '1' || s.charAt(i) == '2'
                        && s.charAt(i + 1) <= '6') {
                    ways[i] += ways[i + 2];
                }
            }
        }
        System.out.println(Arrays.toString(ways));

        return ways[0];
    }

    //GeeksForGeeks
    static int countDecodingDP(char[] digits, int n) {
        int count[] = new int[n + 1]; // A table to store results of subproblems
        count[0] = 1;
        count[1] = 1;

        for (int i = 2; i <= n; i++) {
            count[i] = 0;

            // If the last digit is not 0, then last digit must add to
            // the number of words
            if (digits[i - 1] > '0')
                count[i] = count[i - 1];

            // If second last digit is smaller than 2 and last digit is
            // smaller than 7, then last two digits form a valid character
            if (digits[i - 2] < '2' || (digits[i - 2] == '2' && digits[i - 1] < '7'))
                count[i] += count[i - 2];
        }
        return count[n];
    }
}
