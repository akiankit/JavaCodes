package com.leetcode;

public class MedianOfTwoSortedArrays {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MedianOfTwoSortedArrays median = new MedianOfTwoSortedArrays();
        int[] ar1 = {
                1, 2,3,4
        };
        int[] ar2 = {

                4, 7,8,9
        };
        System.out.println(median.getMedianForTwoArrays(ar1, 0, ar1.length - 1, ar2, 0,
                ar2.length - 1));
        int med = median.getMedianOfTwoDifferentSizedArrays(ar1, 0, ar1.length - 1, ar2, 0,
                ar2.length - 1);
        System.out.println(med);
    }

    public int median2(int a, int b) {
        return (a+b)/2;
    }
    
    public int median3(int a, int b, int c) {
        return (a + b + c - Math.max(a, Math.max(b, c)) - Math.min(a, Math.min(b, c)));
    }
    
    public int median4(int a, int b, int c, int d) {
        return (a + b + c + d - Math.max(d, Math.max(a, Math.max(b, c))) - Math.min(d,
                Math.min(a, Math.min(b, c)))) / 2;
    }
    
    // http://www.geeksforgeeks.org/median-of-two-sorted-arrays-of-different-sizes/
    public int getMedianOfTwoDifferentSizedArrays(int[] arr1, int s1, int e1, int[] arr2, int s2,
            int e2) {
        int N = e1 - s1 + 1;
        int M = e2 - s2 + 1;
        if (N == 1) {
            if (M == 1)
                return median2(arr1[0], arr2[0]);
            else if ((M & 1) == 0) {
                return median3(arr2[M / 2 - 1], arr2[M / 2], arr1[0]);
            } else {
                return median2(arr2[M / 2], median3(arr2[M / 2 - 1], arr2[M / 2 + 1], arr1[0]));
            }
        } else if (N == 2) {
            if (M == 2) {
                return median4(arr1[0], arr1[1], arr2[0], arr2[1]);
            } else if ((M & 1) == 0) {
                // B[M/2], B[M/2 � 1], max(A[0], B[M/2 � 2]), min(A[1], B[M/2 + 1])
                return median4(arr2[M / 2], arr2[M / 2 - 1], Math.max(arr1[0], arr2[M / 2 - 2]),
                        Math.min(arr1[1], arr2[M / 2 + 1]));
            } else {
                return median3(arr2[M / 2], Math.max(arr1[0], arr2[M / 2 - 1]),
                        Math.min(arr1[1], arr2[M / 2 + 1]));
            }
        }
        int idxA = (s1 + e1) / 2;
        int idxB = (s2 + e2) / 2;
        // if A[idxA] <= B[idxB], then median must exist in
        // A[idxA....] and B[....idxB]
        if (arr1[idxA] <= arr2[idxB]) {
            int ignoredLength = (N/2);
            return getMedianOfTwoDifferentSizedArrays(arr1, idxA, e1, arr2, s2, e2-ignoredLength);
        }
        // if A[idxA] > B[idxB], then median must exist in
        // A[...idxA] and B[idxB....]
        else {
            int ignoredLenght = N/2;
            return getMedianOfTwoDifferentSizedArrays(arr1, s1, idxA, arr2, s2 + ignoredLenght - 1,
                    e2);
        }
    }

    // http://www.geeksforgeeks.org/median-of-two-sorted-arrays/
    public int getMedianForTwoArrays(int[] arr1, int s1, int e1, int[] arr2, int s2, int e2) {
//        System.out.println("arr1(" + s1 + "-" + e1 + "),arr2(" + s2 + "-" + e2 + ")");
        if (e1 < s1)
            return 0;
        if (e1 == s1)
            return (arr1[s1] + arr1[s2]) / 2;
        if (e2 - s2 == 1)
            return medianForTwoSizeArray(arr1, s1, e1, arr2, s2, e2);
        int m1 = getMedian(arr1, s1, e1);
        int m2 = getMedian(arr2, s2, e2);
//        System.out.println("m1="+m1+" m2="+m2);
        if (m1 == m2)
            return m1;
        int len = e1 - s1 + 1;
        if (m1 > m2) {
            // if m1 > m2 then median must exist in ar1[....m1] and ar2[m2...]
            if ((len & 1) == 0) {
                return getMedianForTwoArrays(arr1, s1, s1 + len / 2, arr2, s2 + len / 2 - 1, e2);
            } else {
                return getMedianForTwoArrays(arr1, s1, s1 + len / 2, arr2, s2 + len / 2, e2);
            }
        } else {
            // if m1 < m2 then median must exist in ar1[m1....] and ar2[....m2]
            if ((len & 1) == 0) {
                return getMedianForTwoArrays(arr1, s1 + len / 2 - 1, e1, arr2, s2, s2 + len / 2);
            } else {
                return getMedianForTwoArrays(arr1, s1 + len / 2, e1, arr2, s2, s2 + len / 2);
            }
        }
    }
    
    private int medianForTwoSizeArray(int[] arr1, int s1, int e1, int[] arr2, int s2, int e2) {
        return (Math.max(arr1[s1], arr2[s2]) + Math.min(arr1[e1], arr2[e2])) / 2;
    }

    public int getMedian(int[] arr, int s, int e) {
        int length = e - s + 1;
        if (length == 1)
            return arr[s];
        if ((length & 1) == 0) {
            return (arr[s + length / 2] + arr[s + length / 2 - 1]) / 2;
        } else {
            return arr[s + length / 2];
        }
    }
}
