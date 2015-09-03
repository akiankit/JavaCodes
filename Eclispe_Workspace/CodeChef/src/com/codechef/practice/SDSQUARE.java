package com.codechef.practice;

import java.util.Scanner;

public class SDSQUARE {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		long[] perfectSquares = {1,4,9,49,100,144,400,441,900,1444,4900,9409,10000,10404,11449,14400,19044,40000,40401,44100,44944,
								90000,144400,419904,490000,491401,904401,940900,994009,1000000,1004004,1014049,1040400,1100401,1144900,1440000,
								1904400,1940449,4000000,4004001,4040100,4410000,4494400,9000000,9909904,9941409,11909401,14010049,14040009,14440000,19909444,40411449,41990400,
								49000000,49014001,49140100,49999041,90440100,94090000,94109401,99400900,99940009,100000000,100040004,100140049,100400400,101404900,
								101949409,104040000,104919049,110040100,111049444,114041041,114490000,144000000,190440000,194044900,400000000,400040001,400400100,
								404010000,404090404,409941009,414000409,414041104,441000000,449440000,490091044,900000000,990990400,991494144,994140900,1190940100,
								1401004900,1404000900,1409101444,1444000000,1449401041,1490114404,1990944400,4014109449l,4019940409l,4041144900l,4199040000l,4900000000l,
								4900140001l,4901400100l,4914010000l,4914991449l,4941949401l,4999904100l,9044010000l,9409000000l,9409194001l,9410940100l,9900449001l,9940090000l,
								9994000900l,9999400009l,10000000000l};
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			long aValue = scanner.nextLong();
			long bValue = scanner.nextLong();
			int count = 0;
			//System.out.println("Length = "+perfectSquares.length);
			for(int j=0;j<perfectSquares.length;j++){
				if(perfectSquares[j] > bValue){
					break;
				}
				if(perfectSquares[j] == aValue || perfectSquares[j] == bValue){
					count++;
				}else if(perfectSquares[j] < bValue && perfectSquares[j] > aValue){
					count++;
				}
			}
			System.out.println(count);
		}
		scanner.close();
	}
	
	/*public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			long aValue = scanner.nextLong();
			long bValue = scanner.nextLong();
			int count = 0;
			String square1 = "";
			long square = (long) Math.pow(aValue, 2);
			for (long j = (long) Math.sqrt(aValue); square <= bValue; j++) {
				if (isItAPerfectSquares(square) == true) {
					square1 = square1 + square + ",";
					count++;
				}
				square = (long) Math.pow(j, 2);
			}
			System.out.println(count);
			System.out.println(square1);
		}
		scanner.close();
	}

	private static boolean isItAPerfectSquares(long square) {
		while (square > 0) {
			long digit = square % 10;
			if (digit != 0 && digit != 1 && digit != 4 && digit != 9) {
				return false;
			}
			square = square / 10;
		}
		return true;
	}*/

}
