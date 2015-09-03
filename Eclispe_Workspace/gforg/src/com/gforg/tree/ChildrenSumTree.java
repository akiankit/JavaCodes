package com.gforg.tree;

import com.structures.ds.BSTNode;

// http://www.geeksforgeeks.org/convert-an-arbitrary-binary-tree-to-a-tree-that-holds-children-sum-property/
public class ChildrenSumTree {

//      50
//    /     \     
//  /         \
//7             2
/// \             /\
///     \          /   \
//3        5      1      30
    public static void main(String[] args) {
        BSTNode node = new BSTNode(50);
        node.left = new BSTNode(7);
        node.right = new BSTNode(2);
        node.left.left = new BSTNode(3);
        node.left.right = new BSTNode(5);
        node.right.left = new BSTNode(1);
        node.right.right = new BSTNode(30);
        BSTNode.printInorder(node);
        System.out.println(isChildrenSumTreeValid(node));
        convertTree(node);
        BSTNode.printInorder(node);
        System.out.println(isChildrenSumTreeValid(node));
    }
    
    public static boolean isChildrenSumTreeValid(BSTNode root) {
        if (root == null || (root.left == null && root.right == null))
            return true;
        int left = 0, right = 0;
        if (root.left != null)
            left = root.left.val;
        if (root.right != null)
            right = root.right.val;
        if (root.val != left + right)
            return false;
        return isChildrenSumTreeValid(root.left) && isChildrenSumTreeValid(root.right);
    }

    public static void convertTree(BSTNode root) {
        if(root == null || (root.left == null && root.right==null))
            return;
        convertTree(root.left);
        convertTree(root.right);
        int left = 0;
        int right = 0;
        int data = root.val;
        if(root.left != null)
            left = root.left.val;
        if(root.right != null) 
            right = root.right.val;
        int childSum = left+right;
        if (childSum > data) {
            root.val = childSum;
        } else if (childSum < data) {
            increament(root, data - childSum);
        }
    }

    private static void increament(BSTNode node, int diff) {
        if(node.left != null) {
            node.left.val = node.left.val + diff;
            increament(node.left, diff);
        } else if (node.right != null) {
            node.right.val = node.right.val + diff;
            increament(node.right, diff);
        }
    }
}
