
package com.categorize.late;

public class Karatsuba {

    public static void main(String[] args) {
        System.out.println(multiplicationUsingKaratsuba(123456789, 112345678));
    }

    // Assuming base is 10;
    static long multiplicationUsingKaratsuba(int a, int b) {
        if (a < 10 || b < 10) {
            return a * b;
        }
        long result = 0l;
        int m = getSizeOfM(a, b);
        int m2 = m / 2;
        if(m2 == 0){
            m2 =1;
            m = 2;
        }
        int[] a1 = divideNumberInTwoParts(a, m2);
        int[] b1 = divideNumberInTwoParts(b, m2);
        int x1 = a1[0];
        int x0 = a1[1];
        int y1 = b1[0];
        int y0 = b1[1];
        /*
         * int z0 = x1 * y1; int z2 = x0 * y0; int z1 = (x1+x0)*(y1+y0)-z0-z2;
         */
        long z0 = multiplicationUsingKaratsuba(x1, y1);
        long z2 = multiplicationUsingKaratsuba(x0, y0);
        long z1 = multiplicationUsingKaratsuba((x1 + x0), (y1 + y0));
        result = (long)(z0 * Math.pow(10, 2*m2) + (z1-z0-z2) * Math.pow(10, m2) + z2);
        return result;
    }

    private static int[] divideNumberInTwoParts(int a, int power) {
        int[] result = new int[2];
        result[0] = (int)(a / Math.pow(10, power));
        result[1] = (int)(a % Math.pow(10, power));
        return result;
    }

    private static int getSizeOfM(int a, int b) {
        return (int)Math.min(getNumOfDigits((long)a), getNumOfDigits((long)b));
    }
    
    static public int getNumOfDigits(Long number) {
        int digitsCount = 0;
        while (number > 0) {
            number = number / 10;
            digitsCount++;
        }
        return digitsCount;
    }
}
