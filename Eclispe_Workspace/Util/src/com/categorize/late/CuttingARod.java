
package com.categorize.late;

//Refer to http://www.youtube.com/watch?v=U-09Gs6cbsQ
public class CuttingARod {

    static int rodLength = 8;

    static int[] cutsPrice = {
            0, 1, 5, 8, 9, 10, 17, 17, 20
    };

    static int[] bestPrice = new int[rodLength + 1];

    static int[] startCut = new int[rodLength + 1];

    public static void main(String[] args) {
        int price = getBestPrice(cutsPrice, rodLength);
        System.out.println(price);
        getBestWay(startCut, rodLength);
    }

    static int getBestPrice(int[] cutsPrice, int rodLength) {
        if (rodLength == 0 || rodLength < 0) {
            return 0;
        }
        int price = 0;
        for (int j = 1; j <= rodLength; j++) {
            price = 0;
            for (int i = 1; i <= j; i++) {
                int tempPrice = cutsPrice[i];
                if (bestPrice[j - i] != 0) {
                    tempPrice += bestPrice[j - i];
                } else {
                    tempPrice += getBestPrice(cutsPrice, j - i);
                }
                if (tempPrice > price) {
                    price = tempPrice;
                    startCut[rodLength] = i;
                }
            }
        }
        bestPrice[rodLength] = price;
        return bestPrice[rodLength];
    }

    static void getBestWay(int[] startCut, int rodLength) {
        while (rodLength > 0) {
            System.out.print(startCut[rodLength] + ",");
            rodLength = rodLength - startCut[rodLength];
        }
    }

}
