package com.structures.ds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class MaxSegmentTree {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MaxSegmentTreeSelfWritten segmentTree = new MaxSegmentTreeSelfWritten();
        int[] arr = {1,2,7,8,5};
        segmentTree.createSegmentTree(arr);
        System.out.println(segmentTree.countOfSmallerNumber(arr, segmentTree.root,1));
        System.out.println(segmentTree.countOfSmallerNumber(arr, segmentTree.root,8));
        System.out.println(segmentTree.countOfSmallerNumber(arr, segmentTree.root,5));
    }

}

// In this case we are doing for maximum values between a range.
// This can easily be modified to store minimum or sum of range.
class MaxSegmentTreeSelfWritten{
    
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
        node.val = Math.max(node.left.val, node.right.val);
        return node;
    }
    
    public int countOfSmallerNumber(int[] A, SegmentTreeNode root, int val) {
        if (root == null || A[root.start] > val) {
            return 0;
        }
        if (root.val < val) {
            return root.end - root.start + 1;
        }
        return countOfSmallerNumber(A, root.left, val) + countOfSmallerNumber(A, root.right, val);
    }
    
    public ArrayList<Integer> countOfSmallerNumber(int[] A, int[] queries) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(A == null || queries == null || queries.length == 0)
            return list;
        Arrays.sort(A);
        root = null;
        SegmentTreeNode root = createSegmentTree(A);
        for(int i=0;i<queries.length;i++) {
            list.add(countOfSmallerNumber(A, root, queries[i]));
        }
        return list;
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
            return Math.max(query(root.left, start, mid), query(root.right, mid + 1, end));

    }
    
    public void modify(SegmentTreeNode root, int index, int value) {
        Stack<SegmentTreeNode> stack = new Stack<SegmentTreeNode>();
//        if(root.start == index && root.end == index)
//            root.val = value;
        boolean isUpdated = false;
        SegmentTreeNode node = root;
        while(!isUpdated){
            if (index == node.start && index == node.end) {
                node.val = value;
                isUpdated = true;
                break;
            }
            stack.push(node);
            int mid = (node.start + node.end)/2;
            if(index <= mid)
                node = node.left;
            else 
                node = node.right;
        }
        while(!stack.isEmpty()) {
            node = stack.pop();
            node.val = Math.max(node.left.val, node.right.val);
        }
        
    }
}

class SegmentTreeCopied{
    
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

    public SegmentTreeCopied(int size) {
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