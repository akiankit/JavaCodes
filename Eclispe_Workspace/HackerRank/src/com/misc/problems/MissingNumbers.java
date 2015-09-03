
package com.misc.problems;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MissingNumbers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int aCount = scanner.nextInt();
        Map<Integer, Integer> aCounts = new HashMap<Integer, Integer>();
        for (int i = 0; i < aCount; i++) {
            int num = scanner.nextInt();
            if (aCounts.get(num) != null) {
                aCounts.put(num, aCounts.get(num) + 1);
            } else {
                aCounts.put(num, 1);
            }
        }
        int bCount = scanner.nextInt();
        Map<Integer, Integer> bCounts = new HashMap<Integer, Integer>();
        for (int i = 0; i < bCount; i++) {
            int num = scanner.nextInt();
            if (bCounts.get(num) != null) {
                bCounts.put(num, bCounts.get(num) + 1);
            } else {
                bCounts.put(num, 1);
            }
        }
        scanner.close();
        List<Integer> missedNumbers = new LinkedList<Integer>();
        Set<Integer> keySet = bCounts.keySet();
        for (Integer key : keySet) {
            if (aCounts.get(key) == null
                    || (aCounts.get(key).intValue() != bCounts.get(key).intValue())) {
                missedNumbers.add(key);
            }
        }
        Collections.sort(missedNumbers);
        for (int i = 0; i < missedNumbers.size(); i++) {
            System.out.print(missedNumbers.get(i) + " ");
        }
    }

}
