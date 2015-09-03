package com.leetcode.arrays;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Sum3 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] num = {
                -2,0,0,2,2
                /*-1, 0, 1, 2, -1, -4*/
                /*7, -1, 14, -12, -8, 7, 2, -15, 8, 8, -8, -14, -4, -5, 7, 9, 11, -4, -15, -6, 1,
                -14, 4, 3, 10, -5, 2, 1, 6, 11, 2, -2, -5, -7, -6, 2, -15, 11, -6, 8, -4, 2, 1, -1,
                4, -6, -15, 1, 5, -15, 10, 14, 9, -8, -6, 4, -6, 11, 12, -15, 7, -1, -9, 9, -1, 0,
                -4, -1, -12, -2, 14, -9, 7, 0, -3, -4, 1, -2, 12, 14, -10, 0, 5, 14, -1, 14, 3, 8,
                10, -8, 8, -5, -2, 6, -11, 12, 13, -7, -12, 8, 6, -13, 14, -2, -5, -11, 1, 3, -6*/
        };
        long start = System.currentTimeMillis();
        System.out.println(threeSum(num));
        long end = System.currentTimeMillis();
        System.out.println("Total time taken is="+(end - start)+"ms");
    }

    public static List<List<Integer>> threeSum(int[] num) {
        Arrays.sort(num);
        List<List<Integer>> result = new LinkedList<List<Integer>>();
        for (int i = 0; i <= num.length - 3; i++) {
            if(i > 0 && num[i] == num[i-1])
                continue;
            int a = num[i];
            int j = i + 1;
            int k = num.length - 1;
            while (k > j && k > i) {
                int b = num[j];
                int c = num[k];
                if (a + b + c == 0) {
                    List<Integer> curr = new LinkedList<Integer>();
                    curr.add(a);
                    curr.add(b);
                    curr.add(c);
                    if(!result.contains(curr))
                        result.add(curr);
//                    System.out.println(curr);
                    j++;
                    k--;
                } else if (a + b + c > 0) {
                    k--;
                } else {
                    j++;
                }
            }
        }
        return result;
    }
}
