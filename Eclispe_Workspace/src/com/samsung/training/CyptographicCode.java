package com.samsung.training;

import java.util.Scanner;

public class CyptographicCode {

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        int T;
        T = sc.nextInt();
        String[] chars = {"0001101","0011001","0010011","0111101","0100011","0110001","0101111","0111011","0110111","0001011"};
        /*int[] digits = {
                3211, 2221, 2122, 1411, 1132, 1231, 1114, 1312, 1213, 3112
        };*/
        for (int test_case = 1; test_case <= T; test_case++) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            String[] input = new String[n];
            for(int i=0;i<n;i++) {
                input[i] = sc.next();
            }
            int size = 56;
            boolean found = false;
            int j=m-1,i=n-1;
            for(;i>=0;i--) {
                j = m-1;
                for(;j>=0;j--) {
                    if(input[i].charAt(j) =='1') {
                        found = true;
                        break;
                    }
                }
                if(found)
                    break;
            }
//            System.out.println("j=" + j);
            int startIndex = j-size+1;
            char prev = 0, curr;
            int tempNum = 0;
            int currCount = 0;
            int[] finalDigits = new int[8];
            int index = 0;
            StringBuilder tempSb = new StringBuilder();
//            System.out.println(startIndex);
            for (int k = 0; k < size; k++) {
                boolean done = false;
                curr = input[i].charAt(startIndex+k);
                /*if(k%7 == 0){
                    currCount=1;
                } else if (prev != curr) {
                    tempNum = tempNum * 10 + currCount;
                    done = true;
                    currCount = 0;
                } else if (k % 7 != 0 && prev == curr) {
                    currCount++;
                }
                prev = curr;*/
                tempSb.append(curr);
                if (k % 7 == 6) {
//                    System.out.print(tempSb.toString());
                    /*if (!done) {
                        tempNum = tempNum * 10 + currCount;
                    }*/
                    int num = findIndex(tempSb.toString(), chars);
//                    System.out.println(" "+num);
                    finalDigits[index++] = num;
//                    tempNum = 0;
//                    currCount = 0;
                    tempSb = new StringBuilder();
                }
            }
//            System.out.println(Arrays.toString(finalDigits));
            int code = 3 * (finalDigits[0] + finalDigits[2] + finalDigits[4] + finalDigits[6])
                    + (finalDigits[1] + finalDigits[3] + finalDigits[5] + finalDigits[7]);
//            System.out.println(code);
            boolean isValid = code % 10 == 0;
//            System.out.println(isValid);
            int ans = isValid ? (finalDigits[0] + finalDigits[2] + finalDigits[4]
                    + finalDigits[6] + finalDigits[1] + finalDigits[3] + finalDigits[5] + finalDigits[7])
                    : 0;
            System.out.println("#" + test_case + " "+ ans);
        }
        sc.close();
    }

    private static int findIndex(String string, String[] chars) {
        for(int i=0;i<chars.length;i++) {
            if (chars[i].equalsIgnoreCase(string))
                return i;
        }
        return 0;
    }

}
