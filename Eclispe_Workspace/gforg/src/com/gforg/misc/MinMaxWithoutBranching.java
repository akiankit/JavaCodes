
package com.gforg.misc;

class MinMaxUsingBitsWithoutBranching {

    public static void main(String[] args) {
        int x = 10;
        int y = 15;
        System.out.println(min(x, y));
        System.out.println(max(x, y));
    }

    public static int min(int x, int y) {
        int result = 0;
        int diff = x - y;
        System.out.println(Integer.toBinaryString(diff));
        System.out.println(Integer.toBinaryString(diff >> 31));
        result = y + (diff & diff >> 31);
        return result;
    }

    public static int max(int x, int y) {
        int result = 0;
        result = x - ((x - y) & (x - y) >> 31);
        return result;
    }
}
