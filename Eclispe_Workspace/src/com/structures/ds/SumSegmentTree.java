package com.structures.ds;

import java.util.ArrayList;
import java.util.Stack;


public class SumSegmentTree {

    public static void main(String[] args) {
//        SumSegmentTreeSelfWritten segmentTree = new SumSegmentTreeSelfWritten();
//        int[] arr = {1,2,7,8,5};
    }

    class Interval {
        int start, end;
        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
    
    public ArrayList<Integer> intervalMinNumber(int[] A, 
            ArrayList<Interval> queries) {
        SumSegmentTreeSelfWritten minTree = new SumSegmentTreeSelfWritten();
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(A == null || queries == null)
            return list;
        com.structures.ds.SumSegmentTreeSelfWritten.SegmentTreeNode root = minTree.createSegmentTree(A);
        for(Interval interval : queries)
            list.add(minTree.query(root, interval.start, interval.end));
        return null;
    }

}

// In this case we are doing for maximum values between a range.
// This can easily be modified to store minimum or sum of range.
class SumSegmentTreeSelfWritten{
    
    class SegmentTreeNode{
        int start,end;
        int val;
        SegmentTreeNode left,right;
    }
    
    SegmentTreeNode root = null;
    
    public SegmentTreeNode createSegmentTree(int[] arr) {
        root = createSegmentTreeUtil(arr, 0, arr.length -1);
        return root;
    }

    private SegmentTreeNode createSegmentTreeUtil(int[] arr, int s, int e) {
        if (s == e) {
            SegmentTreeNode node = new SegmentTreeNode();
            node.start = s;
            node.end = e;
            node.val = arr[s];
            return node;
        }
        int mid = (s + e) / 2;
        SegmentTreeNode node = new SegmentTreeNode();
        node.start = s;
        node.end = e;
        node.left = createSegmentTreeUtil(arr, s, mid);
        node.right = createSegmentTreeUtil(arr, mid + 1, e);
        node.val = node.left.val + node.right.val;
        return node;
    }
    
    public int query(SegmentTreeNode root, int start, int end) {
        if (root.start > end || root.end < start)
            return 0;
        else if (start <= root.start && end >= root.end)
            return root.val;
        int mid = (root.start + root.end) / 2;
        if (end <= mid)
            return query(root.left, start, end);
        else if (start > mid)
            return query(root.right, start, end);
        else
            return query(root.left, start, mid) +query(root.right, mid + 1, end);

    }
    
    public void modify(SegmentTreeNode root, int index, int value) {
        Stack<SegmentTreeNode> stack = new Stack<SegmentTreeNode>();
        boolean isUpdated = false;
        SegmentTreeNode node = root;
        while (!isUpdated) {
            if (index == node.start && index == node.end) {
                node.val = value;
                isUpdated = true;
                break;
            }
            stack.push(node);
            int mid = (node.start + node.end) / 2;
            if (index <= mid)
                node = node.left;
            else
                node = node.right;
        }
        while (!stack.isEmpty()) {
            node = stack.pop();
            node.val = node.left.val + node.right.val;
        }

    }
}

