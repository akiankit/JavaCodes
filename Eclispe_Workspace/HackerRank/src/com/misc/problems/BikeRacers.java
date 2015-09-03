package com.misc.problems;

import java.util.Arrays;
import java.util.Scanner;

public class BikeRacers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int K = scanner.nextInt();
        Point[] bikers = new Point[N];
//        long[] bikerX = new long[N];
//        long[] bikerY = new long[N];
        
        Point[] bikes = new Point[M];
//        long[] bikesX = new long[M];
//        long[] bikesY = new long[M];
        for (int i = 0; i < N; i++) {
            long a = scanner.nextLong();
            long b = scanner.nextLong();
            bikers[i] = new Point(a, b);
        }
        for (int i = 0; i < M; i++) {
            long a = scanner.nextLong();
            long b = scanner.nextLong();
            bikes[i] = new Point(a, b);
        }
        Arrays.sort(bikers);
        Arrays.sort(bikes);
        Distance[] distances = new Distance[Math.min(N, M)];
        for (int i = 0; i < distances.length; i++) {
            long d = (bikers[i].x - bikes[i].x) * (bikers[i].x - bikes[i].x)
                    + (bikers[i].y - bikes[i].y) * (bikers[i].y - bikes[i].y);
            Distance distance = new Distance(d, i);
            distances[i] = distance;
        }
        Arrays.sort(distances);
        System.out.println(distances[K-1].distance);
        scanner.close();
    }

}

class Distance implements Comparable<Distance> {
    long distance;

    int index;

    Distance(long distance, int index) {
        this.distance = distance;
        this.index = index;
    }

    @Override
    public int compareTo(Distance d) {
        return (int)(this.distance - d.distance);
    }

}

class Point implements Comparable<Point> {

    long x;

    long y;

    Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point o) {
        long d1 = x * x + y * y;
        long d2 = o.x * o.x + o.y * o.y;
        return (int)(d1 - d2);
    }

}
