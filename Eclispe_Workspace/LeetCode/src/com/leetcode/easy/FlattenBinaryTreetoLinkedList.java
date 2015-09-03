package com.leetcode.easy;

import java.util.Stack;

import com.leetcode.util.TreeNode;

public class FlattenBinaryTreetoLinkedList {


//    1
//    / \
//   2   5
//  / \   \
// 3   4   6
    public static void main(String[] args) {
        TreeNode node = new TreeNode(1);
        node.left = new TreeNode(2);
        node.left.left = new TreeNode(3);
        node.left.right = new TreeNode(4);
        node.right = new TreeNode(5);
//        node.right.right = new TreeNode(6);
        preorderTraversal(node);
        System.out.println("hello");
    }

    public void flatten(TreeNode root) {
        
    }
    
    public static void preorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        if (root == null)
            return;
        else {
            TreeNode newRoot = null;
            TreeNode temp = null;
            while (true) {
                while (root != null) {
                    if(newRoot == null){
                        newRoot = new TreeNode(root.val);
                        newRoot.left = null;
                        temp = newRoot;
                        temp.left = null;
                    }
                    else{
                        temp.right = root;
                        temp.left = null;
                        temp = root;
                    }
                    stack.push(root);
                    System.out.println("Push="+stack);
                    root = root.left;
                }
                if (stack.isEmpty() == true) {
                    break;
                }
                root = stack.pop();
                System.out.println("Pop=" +stack);
                root = root.right;
            }
            root = newRoot;
        }
    }
}
