package com.datastructure.ds;

public class BST {
	 
    BSTNode root;
 
    public BSTNode find(int n) {
        if (root == null)
            return null;
        BSTNode temp = root;
        while (true) {
            if (n == temp.val)
                return temp;
            if (n > temp.val) {
                if (temp.right != null)
                    temp = temp.right;
                else
                    return null;
            } else {
                if (temp.left != null)
                    temp = temp.left;
                else
                    return null;
            }
        }
    }
 
    public BSTNode getMin(BSTNode node) {
        if (node == null)
            return null;
        if (node != null && node.left == null)
            return node;
        return getMin(node.left);
    }
 
    public void addArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            addVal(arr[i]);
        }
    }
 
    public void addVal(int n) {
        addNode(new BSTNode(n));
    }
 
    public void addNode(BSTNode node) {
        if (root == null) {
            root = node;
            return;
        }
        BSTNode temp = root;
        while (true) {
            if (node.val > temp.val) {
                if (temp.right == null) {
                    temp.right = node;
                    return;
                }
                temp = temp.right;
            } else {
                if (temp.left == null) {
                    temp.left = node;
                    return;
                }
                temp = temp.left;
            }
        }
    }
 
    public BSTNode getRoot() {
        return root;
    }
 
    @Override
    public String toString() {
        return root.toString();
    }
}

class BSTNode {
    BSTNode left, right;
 
    int val;
 
    public BSTNode(int val) {
        this.val = val;
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this == null)
            return "null";
        if (left != null) {
            sb.append("left=" + left.val);
        } else {
            sb.append("left=null");
        }
        sb.append(",val=" + val);
        if (right != null) {
            sb.append(",right=" + right.val);
        } else {
            sb.append(",right=null");
        }
        return sb.toString();
    }
}