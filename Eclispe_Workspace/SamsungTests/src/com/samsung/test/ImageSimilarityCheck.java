package com.samsung.test;

import java.util.Scanner;

public class ImageSimilarityCheck {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T;
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            double length = sc.nextInt();
            String first = sc.next();
            String second = sc.next();
            double lcs = getLCSLength(first, second);
            double hunderd = 100;
            double res = (lcs / length)*hunderd;
            System.out.println("#" + test_case + " " + String.format("%.2f", res));
        }
        sc.close();
    }
    
    public static int getLCSLength(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] lcsLength = new int[firstLength + 1][secondLength + 1];
        for (int i = 0; i < firstLength + 1; i++) {
            lcsLength[i][0] = 0;
        }
        for (int i = 0; i < secondLength + 1; i++) {
            lcsLength[0][secondLength] = 0;
        }
        for (int i = 1; i < firstLength + 1; i++) {
            for (int j = 1; j < secondLength + 1; j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    lcsLength[i][j] = lcsLength[i - 1][j - 1] + 1;
                } else {
                    lcsLength[i][j] = Math.max(lcsLength[i - 1][j], lcsLength[i][j - 1]);
                }
            }
        }
        /*for (int i = 0; i < firstLength + 1; i++) {
            for (int j = 0; j < secondLength + 1; j++) {
                System.out.print(lcsLength[i][j]+" ");
            }
            System.out.println();
        }*/
        return lcsLength[first.length()][second.length()];
    }

}
