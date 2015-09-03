package com.leetcode.tree.medium;

public class CountCompleteTreeNodes {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode node = new TreeNode(4);
        node.left = new TreeNode(5);
//        node.right = new TreeNode(6);
        System.out.println(countNodes(node, 0));
    }

    
      public static class TreeNode {
          int val;
          TreeNode left;
          TreeNode right;
          TreeNode(int x) { val = x; }
      }
     
    public static int totalLeafNodes(TreeNode root) {
        if (root == null)
            return 0;
        if (root.left == null && root.right == null)
            return 1;
        return totalLeafNodes(root.left) + totalLeafNodes(root.right);
    }

    public static int countNodes(TreeNode root, int currentCount) {
        if (isLeafNode(root)) {
            return currentCount + totalLeafNodes(root);
        }else{
            if(currentCount ==0) currentCount = 1;
            return countNodes(root.left, 2*currentCount);
        }
        
    }

    private static boolean isLeafNode(TreeNode root) {
        if (root.left == null && root.right == null)
            return true;
        return false;
    }
}
