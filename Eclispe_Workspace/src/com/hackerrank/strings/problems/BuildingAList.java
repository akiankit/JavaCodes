package com.hackerrank.strings.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BuildingAList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tests = sc.nextInt();
        for (int i = 0; i < tests; i++) {
            sc.nextInt();
            String str = sc.next();
            List<String> list = generateAll(str.toCharArray());
            StringBuilder sb = new StringBuilder();
//            System.out.println(list);
            for (String string : list) {
                sb.append(string).append("\n");
            }
            System.out.println(sb.toString());
        }
        sc.close();
    }

    static private List<String> generateAll(char[] array) {
        List<String> list = new ArrayList<String>();
        Arrays.sort(array);
        for (int i = array.length - 1; i >= 0; i--) {
            char c = array[i];
            List<String> list1 = new ArrayList<String>();
            String str1 = "" + c;
            //System.out.println(str1);
            list1.add(str1);
            for (String string : list) {
                list1.add(str1 + string);
            }
            list1.addAll(list);
            list = new ArrayList<String>(list1);
        }
        return list;
    }}
