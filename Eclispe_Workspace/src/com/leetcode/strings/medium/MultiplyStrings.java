package com.leetcode.strings.medium;

import java.util.LinkedList;
import java.util.List;

public class MultiplyStrings {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(multiplyStrings("1234567", "7654321"));
        System.out.println(multiplyStrings("8383721524", "8"));
    }

    public static String multiplyStrings(String num1, String num2) {
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
        // System.out.println();
        for (String partial : partialMulti) {
            multiplication = addTwoLargeNumbers(multiplication, partial);
            // System.out.print(multiplication + ",");
        }
        // System.out.println();
        return multiplication;
    }

    static public String addLargeNumbersOfSameLengthUseBlock(String num1, String num2) {
        // StringBuilder sum = new StringBuilder();
        List<String> sum = new LinkedList<String>();
        int blockSize = String.valueOf(Integer.MAX_VALUE).length() - 1;
        int lengthOfNumber = num1.length();
        if (lengthOfNumber <= blockSize)
            return String.valueOf(Integer.parseInt(num1) + Integer.parseInt(num2));
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
                StringBuilder sb = new StringBuilder();
                for (int m = 0; m < diff; m++) {
                    sb.append('0');
                }
                tempSum = sb.toString() + tempSum;
            }
            sum.add(0, tempSum);
        }
        StringBuilder finalSum = new StringBuilder();
        for (String string : sum) {
            finalSum.append(string);
        }
        return finalSum.toString();
    }

    static public String addTwoLargeNumbers(String num1, String num2) {
        if (num1.length() == num2.length())
            return addLargeNumbersOfSameLengthUseBlock(num1, num2);
        else
            return addLargeNumbersOfDifferentLength(num1, num2);
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
}
