
package com.gforg.misc;

public class NextHigherNumberWithSameNumberOfSetBits {

    public static void main(String[] args) {
//         for(int i=1;i<=10;i++)
//         System.out.println("i="+i+"Next Highest="+nextHighestNumberWithSameBitsCount(i));
        System.out.println(nextHighestNumberWithSameBitsCount(1918));
    }

    public static int nextHighestNumberWithSameBitsCount(int n) {
        int m = 1;
        int three = 3;
        // In case number is one less than exact power of 2 
        if ((n & (n + 1)) == 0) {
            return (n + 1) | n >> 1;
        } else if ((n & n - 1) == 0) {// If number is power of 2 then just double it.
            return n << 1;
        } else {
            //Other wise search for patter "01" and change it to "10"
            boolean oneFound = false;
            boolean patternFound = false;
            int count = 0;
            int temp = n;
            while (temp != 0) {
                if ((n & m) == m) {
                    oneFound = true;
                } else if (oneFound && (n & m) == 0) {
                    patternFound = true;
                    break;
                } else {
                    oneFound = false;
                }
                m = m << 1;
                count++;
                temp = temp>>1;
            }
            // If pattern "01" is found then change it to "10" using XOR operation.
            if (patternFound) {
                return n ^ (three << (count - 1));
            } else {
                // If pattern "01" is not found that means number is of the form "1100"
                // In that case we can get result by ((1<<4) | (1100>>3)) 
                // shift 1 by number of bits in number(which is 4 here) and 
                // shift number by position of first 1(which is 3 here) and  
                // then take OR of these two
                count = 0;
                m = 1;
                while ((n & m) != m) {
                    m <<= 1;
                    count++;
                }
                int rightPart = n >> (count + 1);
                m = 1;
                while (n != 0) {
                    n = n >> 1;
                    m = m << 1;
                }
                return m | rightPart;
            }
        }
    }
}
