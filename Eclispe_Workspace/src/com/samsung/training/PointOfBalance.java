package com.samsung.training;

import java.util.Scanner;

public class PointOfBalance {

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        int T;
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            int n = sc.nextInt();
            double[] point = new double[n];
            double[] masses = new double[n];
            for (int i = 0; i < n; i++)
                point[i] = sc.nextInt();
            for (int i = 0; i < n; i++)
                masses[i] = sc.nextInt();
            System.out.print("#" + test_case);
            for (int i = 1; i < n; i++) {
//                System.out.printf(" %.10f",bin_search(coord[i - 1], coord[i], i, n));
                System.out.printf(" %.10f", findPoint(point[i-1], point[i], i - 1, i, masses, point));
            }
            System.out.println();
        }
        sc.close();
    }
    
    static double roundValueForPrecision(double k) {
        return Math.round(k * 1e9) / 1e9;
    }

    static double getForce(int i, double mid, double[] masses, double[] points) {
        return roundValueForPrecision(masses[i] / ((mid - points[i]) * (mid - points[i])));
    }

    private static double findPoint(double point, double point2, int start, int end, double[] masses,
            double[] point3) {
        double midPoint = (point + point2) / 2;
        double leftForce = 0;
        double rightForce = 0;
        for (int i = 0; i < masses.length; i++) {
            double diff = point3[i] - midPoint;
            if (diff < 0.00) {
                leftForce = leftForce + getForce(i, midPoint, masses, point3);
            } else {
                rightForce = rightForce + getForce(i, midPoint, masses, point3);
            }
        }
        if (Math.abs(leftForce - rightForce) <= 1e-10)
            return midPoint;
        else if (leftForce - rightForce > 1e-10) {
            return findPoint(midPoint, point2, start, end, masses, point3);
        } else {
            return findPoint(point, midPoint, start, end, masses, point3);
        }

    }

}
