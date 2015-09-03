package com.codechef.practice;

import java.util.Scanner;

public class GCD2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCasesCount = scanner.nextInt();
		for (int i = 0; i < testCasesCount; i++) {
			String num1 = scanner.next();
			String num2 = scanner.next();
			System.out.println(findGCD(num1,num2));
		}
		scanner.close();
	}

	private static String findGCD(String num1, String num2) {
		if(num2.equalsIgnoreCase("")){
			return num1;
		}else{
			return findGCD(num2,getRemainderByDividing(num1, num2));
		}
	}
	
	public static String getRemainderByDividing(String dividend,String divisor){
		String[] divisionResult = divideTwoNumbers(dividend, divisor);
		return divisionResult[1];
	}

	public static String[] divideTwoNumbers(String dividend, String divisor) {
		StringBuilder result = new StringBuilder();
		boolean done = false;
		int length = divisor.length();
		int index=length;
		int dividendLength = dividend.length();
		if(false ==  isResultPositive(dividend, divisor, dividendLength,length)){
			return new String[]{"0",dividend};
		}
		String temp = dividend.substring(0,length);
		while(done == false){
			while(false == isResultPositive(temp,divisor,temp.length(),length)){
				result.append("0");
				if(index >=dividendLength){
					break;
				}
				temp = temp + dividend.substring(index, index+1);
				index++;
			}
			int count =0;
			while(true == isResultPositive(temp, divisor,temp.length(),length)){
				temp = subtractTwoNumbers(temp, divisor);
				count++;
			}
			if(temp.equalsIgnoreCase("0")){
				temp = "";
			}
			if(count >0){
				result.append(count);
			}
			if(index >= dividendLength){
				done = true;
				break;
			}else{
				temp = temp + dividend.substring(index, index+1);
				index++;
			}
			 
		}
		return new String[]{removeLeadingZeroes(result.reverse()).reverse().toString(),temp};
	}
	
	private static boolean isResultPositive(String num1, String num2,int length) {
		for (int i = 0; i < length; i++) {
			if (num1.charAt(i) > num2.charAt(i))
				return true;
			if (num1.charAt(i) < num2.charAt(i))
				return false;
			else
				continue;
		}
		return true;
	}
	
	private static boolean isResultPositive(String num1,String num2,int length1,int length2){
		StringBuilder newNum= new StringBuilder();
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			return isResultPositive(num1, newNum.toString(), length1);
		} else if(length2 > length1) {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			return isResultPositive(newNum.toString(), num2, length2);
		}else{
			return isResultPositive(num1, num2, length1);
		}
	}
	
	private static StringBuilder removeLeadingZeroes(StringBuilder result) {
		int i = result.length() - 1;
		for (i = result.length() - 1; i > 0; i--) {
			if (result.charAt(i) != '0') {
				break;
			}
		}
		result.replace(i + 1, result.length(), "");
		return result;
	}
	
	public static String subtractTwoNumbers(String num1, String num2) {
		String result = "";
		int length1 = num1.length();
		int length2 = num2.length();
		if (length1 == length2) {
			result = subTractTwoNumbersOfSameLength(num1, num2, length1);
		} else {
			result = subTractTwoNumbersOfDifferentLength(num1, num2, length1, length2);
		}
		return result;
	}

	private static String subTractTwoNumbersOfDifferentLength(String num1, String num2, int length1, int length2) {
		String result = "";
		StringBuilder newNum = new StringBuilder();
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			result = subTractTwoNumbersOfSameLength(num1, newNum.toString(), length1);
		} else {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			result = subTractTwoNumbersOfSameLength(newNum.toString(), num2, length2);
		}
		return result;
	}
	
	private static String subTractTwoNumbersOfSameLength(String num1, String num2, int length) {

		StringBuilder result = new StringBuilder();
		int carry = 0;
		boolean resultPositive = isResultPositive(num1, num2, length);
		if (resultPositive == false) {
			String temp = num1;
			num1 = num2;
			num2 = temp;
		}
		for (int i = length - 1; i >= 0; i--) {
			int temp = 0;
			int digit1 = num1.charAt(i) - 48;
			int digit2 = num2.charAt(i) - 48;
			if (digit1 >= digit2) {
				if (carry == 1) {
					digit1--;
					carry = 0;
					if (digit1 + 1 == digit2) {
						digit1 += 10;
						carry = 1;
					}
				}
				temp = digit1 - digit2;
			} else {
				if (carry == 0) {
					carry = 1;
					digit1 += 10;
				} else {
					digit1 = digit1 - 1 + 10;
				}
				temp = digit1 - digit2;
			}
			result.append(temp);
		}
		result = removeLeadingZeroes(result);
		if (resultPositive == false) {
			result.append("-");
		}
		result = result.reverse();
		return result.toString();
	}

}
