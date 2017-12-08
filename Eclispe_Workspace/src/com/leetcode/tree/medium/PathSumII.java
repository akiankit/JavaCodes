package com.leetcode.tree.medium;

import java.util.LinkedList;
import java.util.List;

public class PathSumII {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode node = new TreeNode(5);
        node.left = new TreeNode(4);
        node.right = new TreeNode(8);
        node.left.left =  new TreeNode(11);
        node.left.left.left = new TreeNode(7);
        node.left.left.right = new TreeNode(2);
        node.right.left = new TreeNode(13);
        node.right.right = new TreeNode(4);
        node.right.right.left = new TreeNode(5);
        node.right.right.right = new TreeNode(1);
        System.out.println(pathSum(node, 22));
    }
    
    public static List<List<Integer>> pathSum(TreeNode root, int sum) {
        LinkedList<List<Integer>> list = new LinkedList<List<Integer>>();
        if(root == null)
            return list;
        LinkedList<Integer> currSol = new LinkedList<Integer>();
        currSol.add(root.val);
        pathSumUtil(root, sum, root.val, currSol, list);
        return list;
    }

    public static List<List<Integer>> pathSumUtil(TreeNode root, int sum, int tempSum, List<Integer> currSol,
            List<List<Integer>> list) {
        if (root == null)
            return list;
        if (sum == tempSum && root.left == null && root.right == null) {
            list.add(new LinkedList<Integer>(currSol));
            return list;
        } 
        if (root.left != null) {
            LinkedList<Integer> curr1 = new LinkedList<Integer>(currSol);
            curr1.add(root.left.val);
            pathSumUtil(root.left, sum, tempSum + root.left.val, curr1, list);
        }
        if (root.right != null) {
            LinkedList<Integer> curr2 = new LinkedList<Integer>(currSol);
            curr2.add(root.right.val);
            pathSumUtil(root.right, sum, tempSum + root.right.val, curr2, list);
        }
        return list;
    }
}

