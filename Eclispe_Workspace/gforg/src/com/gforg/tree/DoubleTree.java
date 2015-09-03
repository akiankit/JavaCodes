package com.gforg.tree;

import com.structures.ds.BSTNode;

public class DoubleTree {

    public static void main(String[] args) {
        BSTNode root = new  BSTNode(1);
        root.left = new BSTNode(2);
        root.right = new BSTNode(3);
        root.left.left = new BSTNode(4);
        root.left.right = new BSTNode(5);
        BSTNode.printInorder(root);
        makeDoubleTree(root);
        System.out.println();
        BSTNode.printInorder(root);
    }

    public static void makeDoubleTree(BSTNode root) {
        if(root == null)
            return;
        BSTNode temp = root.left;
        root.left = new BSTNode(root.val);
        root.left.left = temp;
        makeDoubleTree(root.left.left);
        makeDoubleTree(root.right);
    }
}
