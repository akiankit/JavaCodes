package com.hiring.capilary;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Vector;

public class RangeSum {
	
	public static void main(String[] args) {
		Person p = new Student();
		p.talk();
		Scanner sc = new Scanner(System.in);
		long l1 = sc.nextLong();
		long l2 = sc.nextLong();
		if (l1 != 0) {
			l1 = l1 - 1;
		}
		System.out.println(sum(l1, l2));
		sc.close();
	}

	public static String sum(long i, long j){
		BigInteger b1 = new BigInteger(String.valueOf(i));
		BigInteger b2 = new BigInteger(String.valueOf(j));
		BigInteger ONE = new BigInteger("1");
		BigInteger TWO = new BigInteger("2");
		BigInteger res = b2.multiply(b2.add(ONE));
//		System.out.println(res.toString());
		res = res.subtract(b1.multiply(b1.add(ONE)));
		res = res.divide(TWO);
		return res.toString();
	}
}

class Person {
	public void talk() {
		System.out.print("I am a Person ");
	}
}

class Student extends Person {
	public void talk() {
		System.out.print("I am a Student ");
	}
}