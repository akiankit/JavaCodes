/*2^15 = 32768 and the sum of its digits is 3 + 2 + 7 + 6 + 8 = 26.

What is the sum of the digits of the number 2^1000*/
package com.euler.initalproblem;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Problem16 {

    private static int calcDigits(String s) {
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            Character c = new Character(s.charAt(i));
            String z = c.toString();
            int j = Integer.parseInt(z);
            sum += j;
        }
        return sum;
    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int i = 0; i < tests; i++) {
            BigInteger two = BigInteger.valueOf(2);
            long pow = scanner.nextLong();
            BigInteger n = two.pow((int)pow);
            System.out.println(calcDigits(n.toString()));

            long end = System.currentTimeMillis();
            System.out.println(end - begin + "ms");
        }
        scanner.close();
    }

	/*public static void main(String[] args) {
	    long start = System.currentTimeMillis();
	    for(int i=2000;i<=2100;i++){
	        calculateLargePower(2, i);
	    }
	    System.out.println("Time taken="+(System.currentTimeMillis()-start));
	    Scanner scanner = new Scanner(System.in);
	    int tests = scanner.nextInt();
	    for(int i=0;i<tests;i++) {
	        long num = 2;
	        long pow = scanner.nextLong();
	        String power = calculateLargePower(num,pow);
	        //System.out.println(power);
	        int sum = 0;
	        for (int j = 0; j < power.length(); j++) {
	            sum += power.charAt(j)-48;
	        }
	        System.out.println(sum);
	    }
		
		scanner.close();
	}*/
	

    static public String calculateLargePower(long num, long b) {
        String res = "1";
        String num1 = String.valueOf(num);
        while (b != 0) {
            if ((b & 1) == 1)
                res = multiplyTwoLargeNumberFaster(res, num1);
            num1 = multiplyTwoLargeNumberFaster(num1, num1);
            b = b >> 1;
        }
        return res;
    }
    
    static public String addTwoLargeNumbers(String num1, String num2) {
        String res = "";
        if (num1.length() == num2.length()) {
            res = addLargeNumbersOfSameLengthUseBlock(num1, num2);
        } else {
            res = addLargeNumbersOfDifferentLength(num1, num2);
        }

        return res;
    }
    
    private static String addLargeNumbersOfDifferentLength(String num1, String num2) {
        int length1 = num1.length();
        int length2 = num2.length();
        StringBuilder newNum = new StringBuilder();
        String sum = "";
        if (length1 > length2) {
            for (int i = 0; i < length1 - length2; i++) {
                newNum.append("0");
            }
            newNum.append(num2);
            sum = addLargeNumbersOfSameLengthUseBlock(num1, newNum.toString());
        } else {
            for (int i = 0; i < length2 - length1; i++) {
                newNum.append("0");
            }
            newNum.append(num1);
            sum = addLargeNumbersOfSameLengthUseBlock(newNum.toString(), num2);
        }
        return sum;
    }
    
    static public String addLargeNumbersOfSameLengthUseBlock(String num1, String num2) {
        // StringBuilder sum = new StringBuilder();
        List<String> sum = new LinkedList<String>();
        int blockSize = String.valueOf(Integer.MAX_VALUE).length() - 1;
        int lengthOfNumber = num1.length();
        if (lengthOfNumber <= blockSize){
            String temp = String.valueOf(Integer.parseInt(num1)+Integer.parseInt(num2)); 
            if (temp.length() < num1.length()) {
                int diff = num1.length() - temp.length();
                temp = num1.substring(0,diff) + temp;
             } 
            return temp;
        }
        // Assuming length of num1 and num2 are same.
        int carry = 0;
        for (int i = lengthOfNumber - 1; i >= 0; i -= blockSize) {
            int startIndex = i - blockSize + 1;
            if (startIndex < 0) {
                startIndex = 0;
            }
            String tempNum1 = num1.substring(startIndex, i + 1);
            String tempNum2 = num2.substring(startIndex, i + 1);
            String tempSum = String.valueOf(Integer.parseInt(tempNum1) + Integer.parseInt(tempNum2)
                    + carry);
            if (tempSum.length() > blockSize && startIndex != 0) {
                carry = 1;
                tempSum = tempSum.substring(1);
            } else {
                carry = 0;
            }
            if (tempSum.length() < tempNum1.length()) {
               int diff = tempNum1.length() - tempSum.length();
               tempSum = tempNum1.substring(0,diff) + tempSum;
            } 
            sum.add(0, tempSum);
        }
        StringBuilder finalSum = new StringBuilder();
        for (String string : sum) {
            finalSum.append(string);
        }
        /*if (!finalSum.toString().equals((new BigInteger(num1).add(new BigInteger(num2))).toString())) {
            System.out.println("temp");
        }*/
        return finalSum.toString();
    }
    
    static public String multiplyTwoLargeNumberFaster(String num1, String num2) {
        int length1 = num1.length();
        int length2 = num2.length();
        if (length1 > length2) {
            String temp = num1;
            num1 = num2;
            num2 = temp;
            length1 = num1.length();
            length2 = num2.length();
        }
        String partialMulti[] = new String[length2];
        String[] multiplications = new String[10];
        for (int i = length2 - 1, k = 0; i >= 0; i--, k++) {
            int temp1 = num2.charAt(i) - 48;
            int carry = 0;
            StringBuilder sb = new StringBuilder();
            // Max no. of different digits in multiplicand can be 10.
            // So storing multiplication of multiplicant by each digit to avoid calculation again
            // and again
            if (multiplications[temp1] != null && !multiplications[temp1].isEmpty()) {
                sb.append(multiplications[temp1]);
            } else {
                for (int j = length1 - 1; j >= 0; j--) {
                    int temp2 = num1.charAt(j) - 48;
                    int multi = temp2 * temp1 + carry;
                    sb.append(multi % 10);
                    carry = multi / 10;
                }
                if (carry != 0) {
                    sb.append(carry);
                }
                sb = sb.reverse();
                multiplications[temp1] = sb.toString();
            }
            for (int l = 0; l < k; l++) {
                sb.append(0);
            }
            partialMulti[k] = sb.toString();
        }
        String multiplication = "";
        for (String partial : partialMulti) {
            multiplication = addTwoLargeNumbers(multiplication, partial);
        }
        return multiplication;
    }
}
