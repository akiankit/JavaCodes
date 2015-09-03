package com.leetcode.medium;

public class BitwiseANDofNumbersRange {

    public static void main(String[] args) {
        System.out.println(rangeBitwiseAnd(10, 17));
    }
    
    static int rangeBitwiseAnd(int m, int n) {
        if (m == n)
            return m;
        else {
            String binaryM = Integer.toBinaryString(m);
            String binaryN = Integer.toBinaryString(n);
            int lengthM = binaryM.length();
            int lengthN = binaryN.length();
            if (lengthM != lengthN)
                return 0;
            else {
                StringBuilder sb = new StringBuilder();
                boolean restAllZeroes = false;
                for (int i = 0; i < lengthM; i++) {
                    if (restAllZeroes == false && binaryM.charAt(i) == binaryN.charAt(i)) {
                        sb.append(binaryM.charAt(i));
                    } else {
                        restAllZeroes = true;
                        sb.append(0);
                    }
                }
                return Integer.parseInt(sb.toString(), 2);
            }
        }
    }

}
