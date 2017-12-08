package com.learning.geeksforgeeks;

import java.util.Stack;

class SegmentTreeNode {
	int start, end;
	long val;
	SegmentTreeNode left, right;
}

class SumSegmentTreeSelfWritten {

	SegmentTreeNode root = null;

	public SegmentTreeNode createSegmentTree(int[] arr) {
		root = createSegmentTreeUtil(arr, 0, arr.length - 1);
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

	public long query(SegmentTreeNode root, int start, int end) {
		if (root.start > end || root.end < start)
			return 0 * 1L;
		else if (start <= root.start && end >= root.end)
			return root.val * 1L;
		int mid = (root.start + root.end) / 2;
		if (end <= mid)
			return query(root.left, start, end);
		else if (start > mid)
			return query(root.right, start, end);
		else
			return query(root.left, start, mid) * 1L
					+ query(root.right, mid + 1, end) * 1L;

	}
	
	public void modify(SegmentTreeNode root, int index, int value) {
        Stack<SegmentTreeNode> stack = new Stack<SegmentTreeNode>();
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
            node.val = node.left.val + node.right.val;
        }
	}
}
