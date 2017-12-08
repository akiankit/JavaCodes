
package com.codechef.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class PALIN {/*

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();
        for(int i=0;i<testCases;i++){
            int num = scanner.nextInt();
            System.out.println(getNextPalindrome(num));
        }
        scanner.close();
    }

    public static long getNextPalindrome(String number) {
        StringBuilder nextPalindrome = new StringBuilder();
        int[] digits = getDigitsInNumber(number);
        if (digits.length == 1) {
            if (digits[0] != 9)
                return digits[0] + 1;
            else
                return 11;
        }
        if(digits.length == 2){
            return getNextPalindromeOf2Digit(digits);
        }
        // Odd case
        int numbers = digits.length;
        long leftHalf = getLeftHalf(digits);
        long rightHalf = getRightHalf(digits);
        if ((numbers & 1) == 1) {
            int midNumber = digits[numbers / 2];
            if (leftHalf > rightHalf) {
                nextPalindrome.append(leftHalf).append(midNumber)
                        .append(getReverseOfNumber(leftHalf));
            } else {
                if (midNumber != 9) {
                    nextPalindrome.append(leftHalf).append(midNumber + 1)
                            .append(getReverseOfNumber(leftHalf));
                } else {
                    leftHalf = leftHalf + 1;
                    nextPalindrome.append(leftHalf).append(getReverseOfNumber(leftHalf));
                }
            }
        } else {
            int mid1 = digits[numbers / 2 - 1];
            int mid2 = digits[numbers / 2];
            int midNumber = mid1*10 + mid2;
            int nextPalindromeOf2Digit = getNextPalindromeOf2Digit(new int[] {
                    mid1, mid2
            });
            if (mid1 != mid2) {
                nextPalindrome.append(leftHalf).append(nextPalindromeOf2Digit)
                        .append(getReverseOfNumber(leftHalf));
            } else {
                if (midNumber != 99) {
                    if (leftHalf > rightHalf)
                        nextPalindrome.append(leftHalf).append(midNumber)
                                .append(getReverseOfNumber(leftHalf));
                    else
                        nextPalindrome.append(leftHalf).append(nextPalindromeOf2Digit)
                                .append(getReverseOfNumber(leftHalf));
                } else {
                    if (leftHalf > rightHalf)
                        nextPalindrome.append(leftHalf).append(midNumber)
                                .append(getReverseOfNumber(leftHalf));
                    else {
                        nextPalindrome.append(leftHalf + 1).append("00")
                                .append(getReverseOfNumber(leftHalf + 1));
                    }
                }
            }

        }
        return Long.parseLong(nextPalindrome.toString());
    }

    public static String getReverseOfNumber(long number) {
        StringBuilder builder = new StringBuilder();
        builder.append(number);
        StringBuilder reverse = builder.reverse();
        return reverse.toString();
    }

    public static int getNextPalindromeOf2Digit(int[] digits) {
        int nextPalindrome = 0;
        long leftHalf = digits[0];
        long rightHalf = digits[1];
        if (leftHalf > rightHalf)
            nextPalindrome = (int)(leftHalf * 10 + leftHalf);
        else {
            if (leftHalf != 9) {
                nextPalindrome = (int)((leftHalf + 1) * 10 + leftHalf + 1);
            } else {
                nextPalindrome = 101;
            }
        }
        return nextPalindrome;
    }

    public static int[] getDigitsInNumber(String Number) {
        List<Integer> digits = new LinkedList<Integer>();
        if (Number == 0) {
            digits.add(0);
        } else {
            while (Number > 0) {
                digits.add((int)(Number % 10));
                Number = Number / 10;
            }
        }
        int[] digitsArray = new int[digits.size()];
        for (int i = digits.size() - 1, k = 0; i >= 0; i--) {
            digitsArray[k++] = digits.get(i);
        }
        return digitsArray;
    }

    private static long getLeftHalf(int[] digits) {
        long leftHalf = 0l;
        int midIndex = 0;
        if ((digits.length & 1) == 0)
            midIndex = digits.length / 2 - 1;
        else
            midIndex = digits.length / 2;
        for (int i = 0; i < midIndex; i++)
            leftHalf = leftHalf * 10 + digits[i];
        return leftHalf;
    }

    private static long getRightHalf(int[] digits) {
        long rightHalf = 0l;
        int midIndex = 0;
        midIndex = digits.length / 2;
        for (int i = midIndex + 1; i < digits.length; i++)
            rightHalf = rightHalf * 10 + digits[i];
        return rightHalf;
    }
*/}
