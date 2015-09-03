package com.gforg.tree;

import java.util.LinkedList;
import java.util.List;

import com.structures.ds.BSTNode;

public class AllRootToLeafPaths {

    public static void main(String[] args) {
        BSTNode node = new  BSTNode(10);
        node.left = new BSTNode(8);
        node.right = new BSTNode(2);
        node.left.left = new BSTNode(3);
        node.left.right = new BSTNode(5);
        node.right.right = new BSTNode(2);
        rootToLeafpaths(node);
    }

    public static void rootToLeafpaths(BSTNode root){
        List<List<Integer>> list = new LinkedList<List<Integer>>();
        rootToLeafPathUtil(root, new LinkedList<Integer>(), list);
        System.out.println(list);
    }

    private static void rootToLeafPathUtil(BSTNode root, LinkedList<Integer> curr,
            List<List<Integer>> list) {
        if (root == null)
            return;
        curr.add(root.val);
        if (root.left == null && root.right == null) {
            list.add(new LinkedList<Integer>(curr));
        }
        LinkedList<Integer> curr1 = new LinkedList<Integer>(curr);
        rootToLeafPathUtil(root.left, curr1, list);
        LinkedList<Integer> curr2 = new LinkedList<Integer>(curr);
        rootToLeafPathUtil(root.right, curr2, list);
    }
}
