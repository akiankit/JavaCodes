
package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class RankingAndEvaluations {

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // Reading input and formating input in desired format.
        String str = in.readLine();
        String[] groups = str.split(",");
        String[] groupNames = new String[groups.length];
        float[][] scores = new float[groups.length][];
        for (int i = 0; i < groups.length; i++) {
            String[] strings = groups[i].split(" ");
            groupNames[i] = strings[0];
            scores[i] = new float[strings.length - 1];
            for (int j = 0, k = 1; k < strings.length; j++, k++) {
                scores[i][j] = Float.parseFloat(strings[k]);
            }
        }
        int[][] ranks = new int[groups.length][scores[0].length];
        for (int i = 0; i < scores[0].length; i++) {
            getRank(scores, i, ranks);
        }
        /*System.out.println(Arrays.toString(groupNames));
        for (int i = 0; i < scores.length; i++) {
            System.out.println(Arrays.toString(scores[i]));
        }
         
        for (int i = 0; i < ranks.length; i++) {
            System.out.println(Arrays.toString(ranks[i]));
        }*/

        int minRank = Integer.MAX_VALUE;
        int minRankGroup = -1;
        for (int i = 0; i < ranks.length; i++) {
            int tempRank = 0;
            for (int j = 0; j < ranks[0].length; j++) {
                tempRank += ranks[i][j];
            }
//            System.out.println("Rank for " + groupNames[i] + " " + tempRank);
            if ((tempRank < minRank) ||(tempRank == minRank && groupNames[i].compareTo(groupNames[minRankGroup]) <0)) {
                minRank = tempRank;
                minRankGroup = i;
            }
        }
        System.out.println(groupNames[minRankGroup]);
    }

    private static void getRank(float[][] scores, int i, int[][] ranks) {
        data[] temp = new data[scores.length];
        for (int j = 0; j < scores.length; j++) {
            temp[j] = new data(scores[j][i], j);
        }
        // System.out.println("Initial="+Arrays.toString(temp));
        Arrays.sort(temp);
        // System.out.println("Sorted="+Arrays.toString(temp));
        int rank = 1;
        float prev = 0f;
        for (int j = 0; j < scores.length; j++) {
            if (prev != 0f && temp[j].data != prev) {
                rank++;
            }
            ranks[temp[j].index][i] = rank;
            prev = temp[j].data;
        }
    }

}

class data implements Comparable<data> {
    float data;

    int index;

    data(float data, int index) {
        this.data = data;
        this.index = index;
    }

    @Override
    public String toString() {
        return "[data=" + data + ",index=" + index + "]";
    }

    @Override
    public int compareTo(data o) {
        float diff = o.data - this.data;
        return diff > 0.0 ? 1 : -1;
    }

}
