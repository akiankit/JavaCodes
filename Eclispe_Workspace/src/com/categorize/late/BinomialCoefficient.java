package com.categorize.late;

import java.util.Arrays;

public class BinomialCoefficient {
    
    static long[][] coefficients;
    
    static {
        long mod = 1000000000;
        coefficients = new long[1001][];
        // Initailizing the values for (1,0) and (1,1)
        coefficients[0] = new long[1];
        coefficients[1] = new long[] {
                1, 1
        };
        int lastArraySize = 0;
        for (int i = 2; i <= 1000; i++) {
            // I is starting from 0. So possible coefficients for 'i' is 'i+2'
            int arraySize = i + 1;
            coefficients[i] = new long[arraySize];
            // for any case coefficient of all (n,0) will be 1;
            coefficients[i][0] = 1;
            int j = 1;
            for (; j < (arraySize + 1) / 2; j++) {
                if (j == lastArraySize && j == arraySize - 1)
                    coefficients[i][j] = (2 * coefficients[i - 1][j - 1]) % mod;
                else
                    coefficients[i][j] = (coefficients[i - 1][j - 1] + coefficients[i - 1][j])
                            % mod;
            }
            int k = j;
            if ((i & 1) == 0) {
                j--;
                k = j;
            }
            for (; k < arraySize && j >= 0; k++, --j) {
                if ((i & 1) != 0) {
                    coefficients[i][k] = coefficients[i][j - 1];
                } else {
                    coefficients[i][k] = coefficients[i][j];
                }
            }
            lastArraySize = arraySize;
            // for all cases coefficient of all (n,n) will be 1.
            // coefficients[i][i+1] = 1;
        }
    }
	
	//TODO change int to some other type so that correct evaluation for bigger numbers is possible.
	// 1000000
	private static int nValue = 10;
	private static int kValue = 5;

    public static void main(String[] args) {
        kValue = Math.min(kValue, nValue - kValue);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients[nValue].length; i++) {
            sb.append(coefficients[nValue][i]).append(" ");
        }
        System.out.println(sb.toString());
    }

    private static long getBinamoialCoefficient(int nValue2, int kValue2) {
        long[][] coefficients = new long[nValue2 + 1][];
        // Initailizing the values for (1,0) and (1,1)
        coefficients[0] = new long[1];
        coefficients[1] = new long[] {
                1l, 1l
        };
        int lastArraySize = 0;
        for (int i = 2; i <= nValue2; i++) {
            // I is starting from 0. So possible coefficients for 'i' is 'i+2'
            int arraySize = (i & 1) == 1 ? (i + 1) / 2 : (i + 2) / 2;
            coefficients[i] = new long[arraySize];
            // for any case coefficient of all (n,0) will be 1;
            coefficients[i][0] = 1;
            for (int j = 1; j < arraySize; j++) {
                if (j == lastArraySize && j == arraySize - 1)
                    coefficients[i][j] = 2 * coefficients[i - 1][j - 1];
                else
                    coefficients[i][j] = coefficients[i - 1][j - 1] + coefficients[i - 1][j];
            }
            lastArraySize = arraySize;
            // for all cases coefficient of all (n,n) will be 1.
            // coefficients[i][i+1] = 1;
        }
        for (int i = 0; i < coefficients.length; i++) {
            System.out.println(Arrays.toString(coefficients[i]));
        }
        return coefficients[nValue2][kValue2];
    }
    
    private static long getBinamoialCoefficient1(int nValue2, int kValue2) {
        long mod = 1000000000;
        long[][] coefficients = new long[nValue2 + 1][];
        // Initailizing the values for (1,0) and (1,1)
        coefficients[0] = new long[1];
        coefficients[1] = new long[] {
                1, 1
        };
        int lastArraySize = 0;
        for (int i = 2; i <= nValue2; i++) {
            // I is starting from 0. So possible coefficients for 'i' is 'i+2'
            int arraySize = i + 1;
            coefficients[i] = new long[arraySize];
            // for any case coefficient of all (n,0) will be 1;
            coefficients[i][0] = 1;
            int j = 1;
            for (; j < (arraySize + 1) / 2; j++) {
                if (j == lastArraySize && j == arraySize - 1)
                    coefficients[i][j] = (2 * coefficients[i - 1][j - 1]) % mod;
                else
                    coefficients[i][j] = (coefficients[i - 1][j - 1] + coefficients[i - 1][j])
                            % mod;
            }
            int k = j;
            if ((i & 1) == 0) {
                j--;
                k = j;
            }
            for (; k < arraySize && j >= 0; k++, --j) {
                if ((i & 1) != 0) {
                    coefficients[i][k] = coefficients[i][j - 1];
                } else {
                    coefficients[i][k] = coefficients[i][j];
                }
            }
            lastArraySize = arraySize;
            // for all cases coefficient of all (n,n) will be 1.
            // coefficients[i][i+1] = 1;
        }
        for (int i = 0; i < coefficients.length; i++) {
            System.out.println(Arrays.toString(coefficients[i]));
        }
        return coefficients[nValue2][kValue2];
    }

}
