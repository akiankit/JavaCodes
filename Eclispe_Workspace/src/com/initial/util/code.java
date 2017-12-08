package com.initial.util;

import java.io.*;

public class code {

    public static void main(String[] args) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            int numOfTestCases = Integer.parseInt(bufferRead.readLine().trim());
            for (int i = 0; i < numOfTestCases; i++) {
                String flatCounts = bufferRead.readLine().trim();
                String[] flats = flatCounts.split(" ");
                int rameshFlatsCount = Integer.parseInt(flats[0]);
                int sureshFlatsCount = Integer.parseInt(flats[1]);
                String line = bufferRead.readLine().trim();
                int[] rameshFlats = new int[rameshFlatsCount];
                int[] sureshFlats = new int[sureshFlatsCount];
                String[] prices = line.split(" ");
                int totalRameshFlatPrice = 0;
                for (int r = 0; r < rameshFlatsCount; r++) {
                    rameshFlats[r] = Integer.parseInt(prices[r]) ;
                    totalRameshFlatPrice += rameshFlats[r];
                }
                line = bufferRead.readLine().trim();
                prices = line.split(" ");
                int totalSureshFlatsPrice = 0;
                for (int s = 0; s < sureshFlatsCount; s++) {
                    sureshFlats[s] = Integer.parseInt(prices[s]) ;
                    totalSureshFlatsPrice += sureshFlats[s];
                }
                int diffInPrice = Math.abs(totalSureshFlatsPrice - totalRameshFlatPrice);
                if (diffInPrice % 2 != 0) {
                    System.out.println("-1");
                } else {
                    String solution = "";
                    int solutionCount = 0;
                    int halfDiff = diffInPrice / 2;
                    for (int r = 0; r < rameshFlatsCount; r++) {
                        for (int s = 0; s < sureshFlatsCount; s++) {
                            int diff = Math.abs(rameshFlats[r] - sureshFlats[s]);
                            if (diff == halfDiff
                                    && ((totalRameshFlatPrice > totalSureshFlatsPrice && rameshFlats[r] > sureshFlats[s]) || (totalRameshFlatPrice < totalSureshFlatsPrice && rameshFlats[r] < sureshFlats[s]))) {
                                solutionCount++;
                                solution = solution + (r + 1) + " " + (s + 1) + " ";
                            }
                        }
                    }
                    if (solutionCount == 0) {
                        System.out.println("-1");
                    } else {
                        System.out.println(solutionCount + " " + solution);
                    }
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
