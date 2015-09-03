package com.strings.problems;

import java.util.Scanner;

public class MorganAndStrings {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            String a = scanner.next();
            String b = scanner.next();
            System.out.println(generateMinLexicographically(a, b));
        }
        scanner.close();
    }
    
    public static String generateMinLexicographically(String a, String b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        if (a.length() == 0)
            return b;
        if (b.length() == 0)
            return a;
        StringBuilder sb = new StringBuilder(a.length() + b.length());
        int i = 0, j = 0;
        while (i < a.length() && j < b.length()) {
            if (a.charAt(i) < b.charAt(j)) {
                sb.append(a.charAt(i));
                i++;
            } else if (a.charAt(i) > b.charAt(j)) {
                sb.append(b.charAt(j));
                j++;
            } else {
                StringBuilder sb1 = new StringBuilder();
                int temp1 = i;
                while (temp1 < a.length() && a.charAt(temp1) == b.charAt(j)) {
                    sb1.append(a.charAt(temp1));
                    temp1++;
                }
                StringBuilder sb2 = new StringBuilder();
                int temp2 = j;
                while (temp2 < b.length() && b.charAt(temp2) == a.charAt(i)) {
                    sb2.append(b.charAt(temp2));
                    temp2++;
                }
                if (temp1 != a.length() && temp2 != b.length()) {
                    if (a.charAt(temp1) < b.charAt(temp2)) {
                        sb.append(sb1);
                        i = temp1;
                    } else {
                        sb.append(sb2);
                        j = temp2;
                    }
                }else if(temp1 == a.length()){
                    i = temp1;
                    sb.append(sb1);
                }else{
                    j = temp2;
                    sb.append(sb2);
                }
            }

        }
        while (i < a.length()) {
            sb.append(a.charAt(i));
            i++;
        }
        while (j < b.length()) {
            sb.append(b.charAt(j));
            j++;
        }
        return sb.toString();
    }

}
