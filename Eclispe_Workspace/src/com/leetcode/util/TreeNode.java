package com.leetcode.util;

public class TreeNode {
    public int val;

    public TreeNode left;

    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(3);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(5);
        root.right.left.left = new TreeNode(7);
        root.right.right = new TreeNode(4);
        root.right.right.left = new TreeNode(6);
        root.right.right.left.left = new TreeNode(8);
        root.right.right.left.right = new TreeNode(9);
        String path = " ";
        printPathFromRoot(root, root.right.right.left.right, path);
        System.out.println(path);

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

    public static void printPathFromRoot(TreeNode root, TreeNode node, String path) {
        if (root != null && root == node) {
            path = path + "->" + root.val;
            System.out.println(path);
        }
        if (root.left != null) {
            printPathFromRoot(root.left, node, path + "->" + root.val);
        }
        if (root.right != null) {
            printPathFromRoot(root.right, node, path + "->" + root.val);
        }
    }
}
