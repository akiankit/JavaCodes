package com.leetcode.easy;

public class CompareVersionNumbers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(compareVersion("13.0", "13"));
        System.out.println(compareVersion("0.1", "1.1"));
        System.out.println(compareVersion("1", "1.1"));
        System.out.println(compareVersion("1", "1.0"));
        System.out.println(compareVersion("0.1", "0.0.1"));
    }
    
    static public int compareVersion(String version1, String version2) {
        int i=0,j=0;
        while(true) {
            int num1 = 0;
            int num2 = 0;
            while(i<version1.length() && version1.charAt(i)!='.') {
                num1 = num1 * 10 + version1.charAt(i) - '0';
                i++;
            }
            while(j<version2.length() && version2.charAt(j)!='.') {
                num2 = num2 * 10 + version2.charAt(j) - '0';
                j++;
            }
            if (num1 == num2 && i >= version1.length() && j >= version2.length())
                return 0;
            if(num1 > num2)
                return 1;
            if(num2> num1)
                return -1;
            i++;
            j++;
        }
    }
}
