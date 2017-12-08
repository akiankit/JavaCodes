package com.bluestone.app.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {
	
	public static String formatPriceIndian(double price , String separator){
		BigDecimal bigDec = BigDecimal.valueOf(price);
		BigDecimal amount = roundUp(bigDec);
		String amountStr = amount.toPlainString();
		int count = amountStr.length();
		String tail = "";
		if(count>3){
			tail = separator + amountStr.substring(count-3);
			amountStr = amountStr.substring(0, count-3);
			count-=3;
			while(count>2){
				tail = separator+amountStr.substring(count-2) +tail;
				amountStr = amountStr.substring(0, count-2);
				count-=2;
			}
		}
		amountStr+=tail;		
		return amountStr;
	}

    public static Object formatPriceIndian(BigDecimal totalPrice, String separator) {
        return formatPriceIndian(totalPrice.doubleValue(), separator);
    }
    
    public static Object formatEMIPriceIndian(BigDecimal totalPrice, String separator, int emiTenureInMonths) {
        double price = totalPrice.doubleValue() / emiTenureInMonths;
        return formatPriceIndian(price, separator);
    }
    
    public static BigDecimal roundUp(BigDecimal number) {
        return number.setScale(0,RoundingMode.UP);
    }
}
