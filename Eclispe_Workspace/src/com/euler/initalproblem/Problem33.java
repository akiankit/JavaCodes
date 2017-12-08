/*The fraction 49/98 is a curious fraction, as an inexperienced mathematician in attempting to simplify it may incorrectly believe that 49/98 = 4/8,
  which is correct, is obtained by cancelling the 9s.
We shall consider fractions like, 30/50 = 3/5, to be trivial examples.
There are exactly four non-trivial examples of this type of fraction, less than one in value, and containing two digits in the numerator and denominator.
If the product of these four fractions is given in its lowest common terms, find the value of the denominator.*/
package com.euler.initalproblem;

public class Problem33 {

    public static void main(String[] args) {
        for (int num = 10; num <= 99; num++) {
            for (int denom = num + 1; denom <= 99; denom++) {
                int number = getCommonNumberIfAny(num, denom);
                if (number != -1) {

                }
            }
        }
    }

    private static int getCommonNumberIfAny(int num, int denom) {
        int b = num % 10;
        int a = num / 10;
        int d = denom % 10;
        int c = denom / 10;
        if (a == d && a != 0)
            return a;
        if (b == c && b != 0)
            return b;
        return -1;
    }

}
