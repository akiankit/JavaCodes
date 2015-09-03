
package com.leetcode.easy;

import java.util.HashMap;

public class HappyNumber {

    static boolean[] happy = new boolean[101];

    static HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

    private static int count;

    public static void main(String[] args) {
        for(int i=1;i<=500;i++){
            System.out.println("Number = "+i+"Is Happy Value="+isHappyUtil(i,new HashMap<Integer, Boolean>()));
        }
        System.out.println(count);
//        Set<Integer> keySet = map.keySet();
//        for (Integer integer : keySet) {
//            System.out.println("Number="+integer+" Is Happy value="+map.get(integer));
//        }
    }

    static boolean isHappy(int n) {
        return isHappyUtil(n, new HashMap<Integer, Boolean>());
    }

    private static boolean isHappyUtil(int n, HashMap<Integer,Boolean> visited) {
        if (map.containsKey(n)){
            count++;
            return map.get(n);
        }
        int newN = 0;
        int nCopy = n;
        while (n > 0) {
            newN += (int)Math.pow(n % 10, 2);
            n = n / 10;
        }
        if (newN == 1) {
            map.put(nCopy, true);
        }
        if (visited.containsKey(newN)) {
            map.put(nCopy, false);
            return false;
        }
        visited.put(nCopy, true);
        boolean result = isHappyUtil(newN,visited);
        map.put(newN, result);
        return result;
    }

}
