package com.samsung.training;

import java.util.Scanner;

public class OptimalPath {

    /**
     * @param args
     */
    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        int T=0;
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            int pointCount = sc.nextInt();
            int officeX = sc.nextInt();
            int officeY = sc.nextInt();
            int homeX = sc.nextInt();
            int homeY = sc.nextInt();
            int pointsX[] = new int[pointCount];
            int pointsY[] = new int[pointCount];
            for (int i = 0; i < pointCount; i++) {
                pointsX[i] = sc.nextInt();
                pointsY[i] = sc.nextInt();
            }
            int[] permutation = new int[pointCount];
            for (int i = 0; i < pointCount; i++) {
                permutation[i] = i;
            }
            OptimalPath path = new OptimalPath();
            int minDistance = getDistanceForPermutation(permutation, pointsX, pointsY, officeX,
                    officeY, homeX, homeY);
//            System.out.println("initial="+minDistance);
            while (path.generateNextPermutation(permutation) != -1) {
                int distance = getDistanceForPermutation(permutation, pointsX, pointsY, officeX,
                        officeY, homeX, homeY);
                if (minDistance > distance) {
//                    System.out.println(Arrays.toString(permutation)+"Distance ="+distance);
                    minDistance = distance;
                }
            }
            //System.out.println(count);
            System.out.println("#" + test_case +" "+ minDistance);
        }
        sc.close();
    }
    
    private static int getDistanceForPermutation(int[] permutation, int[] pointsX, int[] pointsY,
            int officeX, int officeY, int homeX, int homeY) {
        int distance = Math.abs(pointsX[permutation[0]] - officeX)
                + Math.abs(pointsY[permutation[0]] - officeY);
        int i = 1;
        for (; i < permutation.length; i++) {
            distance = distance + Math.abs(pointsX[permutation[i]] - pointsX[permutation[i - 1]])
                    + Math.abs(pointsY[permutation[i]] - pointsY[permutation[i - 1]]);
        }
        distance = distance + Math.abs(pointsX[permutation[i - 1]] - homeX)
                + Math.abs(pointsY[permutation[i - 1]] - homeY);
        return distance;
    }

    private int generateNextPermutation(int[] permutation) {
        int num1Index = findCorruptedElement(permutation);
        int num1=0;
        if(num1Index != -1) {
            num1 = permutation[num1Index];
        }else{
            return -1;
        }
        int num2Index = findNextGreater(permutation, num1);
        if(num2Index == -1) {
            return -1;
        }
        swap(permutation, num1Index,num2Index);
        countSort(permutation, num1Index+1, permutation.length-1);
        return 0;
    }
    
    private void swap(int[] permutation, int num1Index, int num2Index) {
        int temp = permutation[num1Index];
        permutation[num1Index] = permutation[num2Index];
        permutation[num2Index] = temp;
    }

    private int findNextGreater(int[] permutation, int num1) {
        for(int i=permutation.length-1;i>0;i--) {
            if(permutation[i] > num1) {
                return i;
            }
        }
        return -1;
    }

    private int findCorruptedElement(int[] permutation) {
        for (int i = permutation.length - 1; i > 0; i--) {
            if (permutation[i - 1] < permutation[i]) {
                return i - 1;
            }
        }
        return -1;
    }

    private void countSort(int[] arr, int start, int end) {
        int[] count = new int[arr.length];
        for (int i = start; i <= end; i++) {
            count[arr[i]]++;
        }
        int k = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] != 0) {
                arr[start + k] = i;
                k++;
                count[i]--;
                i--;
            }
        }
    }

}
