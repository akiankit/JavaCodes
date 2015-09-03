
package com.leetcode.easy;

public class RectangleArea {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(computeArea(-2, -2, 2, 2, 2, -2, 4, 2));
    }

    public static int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
        int totalArea = Math.abs((C - A) * (D - B)) + Math.abs((G - E) * (H - F));
        int bigger = isOneInsideOther(A, B, C, D, E, F, G, H);
        if (bigger != 0)
            return Math.abs(bigger);
        // http://discuss.codechef.com/questions/37269/cake1am-editorial
        int x1 = Math.max(A, E);
        int y1 = Math.max(B, F);
        int x2 = Math.min(C, G);
        int y2 = Math.min(D, H);
        if (x1 < x2 && y1 < y2) {
            return totalArea - Math.abs((x2 - x1) * (y2 - y1));
        }
        return totalArea;
    }

    private static int isOneInsideOther(int a, int b, int c, int d, int e, int f, int g, int h) {
        if (a >= e && b >= f && c <= g && h >= d)
            return (h - f) * (g - e);
        else if (e >= a && f >= b && g <= c && h <= d)
            return (c - a) * (d - b);
        return 0;
    }
}
