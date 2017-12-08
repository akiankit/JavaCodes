package com.bluestone.app.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptographyUtil {
    private static final Logger log = LoggerFactory.getLogger(CryptographyUtil.class);

	/*
	 * Each of these two strings must contain the same characters, but in a
	 * different order. Use only printable characters from the ASCII table. Do
	 * not use single quote, double quote or backslash as these have special
	 * meanings. Each character can only appear once in each string.Both must be
	 * of same length
	 */
	public static String scramble1 = "UKAH652LMOQFBDIEG03JT17N4C89XPVWRSYZ";
																			
																			
																			
																			
	public static String scramble2 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String key = "EUQINHCET1NOITPIRCED1NOITPYRCNE1GNISU1ROF1YEK1MODNAR1ENOTSEULB1A";
																			
	public static BigDecimal adj = new BigDecimal(1.75); // 1st adjustment value (optional)
	public static BigDecimal mod = new BigDecimal(3);// 2nd adjustment value (optional)


	// decrypt string into its original form
	public static String decrypt(String source) {
        log.debug("CryptographyUtil.decrypt():{}", source);
		// convert $key into a sequence of numbers
		List<BigDecimal> fudgefactor = convertKey(key);
		String target = "";
		BigDecimal factor2 = new BigDecimal(0);

		char[] charArray = source.toCharArray();

		for (int i = 0; i < source.length(); i++) {
			// extract a (multibyte) character from $source
			char char2 = charArray[i];

			int num2 = scramble2.indexOf(char2);

			// get an adjustment value using $fudgefactor
			BigDecimal adj = applyFudgeFactor(fudgefactor);

			BigDecimal factor1 = factor2.add(adj);
			long num1 = num2 - Math.round(factor1.doubleValue());
			num1 = checkRange(num1); // check range
			factor2 = factor1.add(new BigDecimal(num2)); 

			char char1 = scramble1.charAt((int) num1);

			target += char1;
		}

		return target.trim();

	} 
	
	// Encrypt string into a garbled form
	public static String encrypt(String source, int sourcelen) {
        log.debug("CryptographyUtil.encrypt()");
		// convert key into a sequence of numbers
		List<BigDecimal> fudgefactor = convertKey(key);
		// Pad source with spaces up to source length
		source = StringUtils.leftPad(source, sourcelen,'0');

		String target = "";
		BigDecimal factor2 = new BigDecimal(0);

		char[] sourceChars = source.toCharArray();
		for (int i = 0; i < source.length(); i++) {
			// extract a multibyte character from source
			char char1;
			char1 = sourceChars[i];

			// identify its position in scramble1
			int num1 = scramble1.indexOf(char1);
			// get an adjustment value using fudgefactor
			BigDecimal adj1 = applyFudgeFactor(fudgefactor);

			BigDecimal factor1 = factor2.add(adj1); // accumulate in factor1

			long num2 = Math.round(factor1.doubleValue()) + num1; // generate
																	// offset
																	// for
																	// scramble2

			num2 = checkRange(num2); // check range

			factor2 = factor1.add(new BigDecimal(num2)); // accumulate in
															// factor2

			// extract multibyte character from scramble2
			char char2 = scramble2.charAt((int) num2);

			// append to target string
			target += char2;
		}

		return target;
	}

	// return an adjustment value based on the contents of fudgefactor
	// NOTE: fudgefactor is passed by reference so that it can be modified
	private static BigDecimal applyFudgeFactor(List<BigDecimal> fudgefactor) {
		BigDecimal fudge = fudgefactor.remove(0);// Extract 1st number from
		fudge = fudge.add(adj);// Add in adjustment value
		fudgefactor.add(fudge);// Put it back at end of array

		// If modifier is supplied and is divisible by modifier, make it
		// negative
		BigDecimal modulo = fudge.multiply(new BigDecimal(100)).remainder(mod);
		if (modulo.compareTo(BigDecimal.ZERO) == 0) {
			fudge = fudge.multiply(new BigDecimal(-1));
		}
		return fudge;
	}

	// Check that num points to an entry in scramble1.Round up to nearest whole
	// number if double is passed
	private static long checkRange(long num) {
		int limit = scramble1.length();

		while (num >= limit) {
			num -= limit; // Value too high, so reduce it
		}
		while (num < 0) {
			num += limit; // Value too low, so increase it
		}
		return num;
	}

	// convert key into an array of numbers
	private static List<BigDecimal> convertKey(String key) {
		List<BigDecimal> arr = new ArrayList<BigDecimal>();
		arr.add(new BigDecimal(key.length())); // first entry in array is length
												// of $key

		int tot = 0;
		char[] keyChars = key.toCharArray();
		for (int i = 0; i < key.length(); i++) {
			// extract a multibyte character from key
			char ch;
			ch = keyChars[i];

			// identify its position in scramble1
			int num = scramble1.indexOf(ch);
			arr.add(new BigDecimal(num)); // store in output array
			tot += num; // accumulate total for later
		}
		arr.add(new BigDecimal(tot));// Insert total as last entry in array
		return arr;
	}

}
