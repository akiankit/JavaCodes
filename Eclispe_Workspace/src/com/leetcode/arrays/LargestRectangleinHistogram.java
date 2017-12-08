package com.leetcode.arrays;

import java.util.Arrays;

public class LargestRectangleinHistogram {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] heights = {2,1,5,6,2,3};
//        System.out.println(largestRectangleArea1(heights));
        int len = 60000;
        heights = new int[len];
        for (int i = 0;i<len;i++) {
            heights[i] = (i+1);
        }
        System.out.println(Arrays.toString(heights));
        System.out.println(largestRectangleArea(heights));
    }
    
    static public int largestRectangleArea(int[] height) {
        return maxArea(height,0,height.length - 1);
    }
    
    // Divide and conquer approach
    private static int maxArea(int[] height, int i, int j) {
        // https://leetcode.com/discuss/22147/simple-divide-and-conquer-ac-solution-without-segment-tree
        if(i==j)
            return height[i];
        int m = i + (j-i)/2;
        // 1 - max area from left half
        int area = maxArea(height, i, m);
        // 2 - max area from right half
        area = Math.max(area, maxArea(height, m+1, j));
        // 3 - max area across the middle
        area = Math.max(area, maxCombineArea(height, i, m, j));
        return area;
    }

    private static int maxCombineArea(int[] height, int s, int m, int e) {
        int i = m;
        int j = m+1;
        int area = 0,h= Math.min(height[m], height[m+1]);
        while (i >= s && j <= e) {
            h = Math.min(h, Math.min(height[i], height[j]));
            area = Math.max(area, (j - i + 1) * h);
            if (i == s)
                ++j;
            else if (j == e)
                --i;
            else {
                if (height[i - 1] > height[j + 1])
                    --i;
                else
                    ++j;
            }
        }
        return area;
    }

    // Complexity is n^2. TLE happens on submission.
    // Naive approach is fine. Need to think something else.
    static public int largestRectangleArea1(int[] height) {
        int[] area = new int[height.length];
        int maxArea = 0;
        for (int i = 0; i < height.length; i++) {
            area[i] = height[i];
            // Traverse in both direction till height less than height[i] is found. All the area
            // till that point can be considered for rectangle area.
            if (i == 0) {
                int j = i + 1;
                while (j < height.length && height[j] >= height[i]) {
                    j++;
                    area[i] += height[i];
                }
            } else if (i == height.length - 1) {
                int k = i - 1;
                while (k >= 0 && height[k] >= height[i]) {
                    k--;
                    area[i] += height[i];
                }
            } else {
                int j = i + 1;
                while (j < height.length && height[j] >= height[i]) {
                    j++;
                    area[i] += height[i];
                }
                int k = i - 1;
                while (k >= 0 && height[k] >= height[i]) {
                    k--;
                    area[i] += height[i];
                }
            }
            if(area[i] > maxArea)
                maxArea = area[i];
        }
        return maxArea;
    }
}

class SegmentTree{
    
    int tree[];
    private int treeSize;
    private int endIndex;
    private int startIndex;
    
    private int leftChild(int i) {
        return 2*i+1;
    }
    
    private int rightChild(int i) {
        return 2*i+2;
    }
    
    private int mid(int start, int end) {
        return (start+ (end-start)/2);
    }

    public SegmentTree(int size) {
        int height = (int)(Math.ceil(Math.log(size) /  Math.log(2)));
        treeSize = 2 * (int) Math.pow(2, height) - 1;
        tree = new int[treeSize];
        endIndex = size - 1; 
    }
    
    public void createSegmentTree(int[] array) {
        createSegmentTreeUtil(array,startIndex,endIndex,0);
        System.out.println(Arrays.toString(tree));
    }

    private int createSegmentTreeUtil(int[] array, int startIndex2, int endIndex2, int i) {
        if (startIndex2 == endIndex2) {
            tree[i] = startIndex2;
            return tree[i];
        }
        int mid = mid(startIndex2, endIndex2);
        int min1 = createSegmentTreeUtil(array, startIndex2, mid, leftChild(i));
        int min2 = createSegmentTreeUtil(array, mid + 1, endIndex2, rightChild(i));
        tree[i] = array[min1] < array[min2] ? min1 : min2;
        return tree[i];
    }
    
    public int getMin(int[] array, int queryStart, int queryEnd) {
        if(queryStart < 0 || queryEnd > tree.length || queryEnd < queryStart)
            return -1;
        return getMinUtil(array,startIndex, endIndex, queryStart,queryEnd,0); 
    }

    private int getMinUtil(int[] array, int startIndex2, int endIndex2, int queryStart, int queryEnd, int i) {
        if (queryStart <= startIndex2 && queryEnd >= endIndex2)
            return tree[i];
        
        if (queryStart > endIndex2 || queryEnd < startIndex2)
            return -1;
        
        int mid = mid(startIndex2, endIndex2);
        int min1 = getMinUtil(array,startIndex2, mid, queryStart, queryEnd, leftChild(i));
        int min2 = getMinUtil(array,mid + 1, endIndex2, queryStart, queryEnd, rightChild(i));
        if(min1 == -1 && min2 == -1)
            return -1;
        if(min1 == -1)
            return min2;
        if(min2 == -1)
            return min1;
        return array[min1] < array[min2] ? min1 : min2;
    }
}