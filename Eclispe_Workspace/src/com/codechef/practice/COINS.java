
package com.codechef.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class COINS {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        String readLine = br.readLine();
        while(readLine!= null && readLine.length() > 0) {
            long coinNumber = Long.parseLong(readLine);
            HashMap<Long, Long> coinValues = new HashMap<Long, Long>();
            System.out.println(getCoinMaxValue(coinNumber, coinValues));
            readLine = br.readLine();
        }
    }

    private static long getCoinMaxValue(long coinNumber, HashMap<Long, Long> coinValues) {
        long coinValue;
        if (coinNumber <= 4) {
            coinValue = coinNumber;
        } else if (coinValues.get(coinNumber) != null) {
            return coinValues.get(coinNumber);
        } else {
            coinValue = Math.max(
                    coinNumber,
                    getCoinMaxValue(coinNumber / 2, coinValues)
                            + getCoinMaxValue(coinNumber / 3, coinValues)
                            + getCoinMaxValue(coinNumber / 4, coinValues));
        }
        coinValues.put(coinNumber, coinValue);
        return coinValue;
    }
}