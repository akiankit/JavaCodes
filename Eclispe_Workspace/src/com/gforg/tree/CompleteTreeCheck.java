package com.gforg.tree;

import java.util.LinkedList;
import java.util.Queue;

import com.structures.ds.BSTNode;

public class CompleteTreeCheck {

    public static void main(String[] args) {
        BSTNode root = new BSTNode(5);
        root.left = new BSTNode(4);
        root.left.left = new BSTNode(3);
        root.left.right = new BSTNode(5);
        root.right = new  BSTNode(6);
        root.right.left = new BSTNode(5);
//        root.right.right = new BSTNode(8);
        System.out.println(isCompleteTree(root));
    }

    // WRONG.
    // NEED TO IMPLEMENT CORRECT METHOD
    public static boolean isCompleteTree(BSTNode root) {
        if (root == null || (root.left == null && root.right != null))
            return false;
        Queue<BSTNode> queue = new LinkedList<BSTNode>();
        queue.add(root);
        queue.add(null);
        int parentCount = 0;
        int childCount = 0;
        while(!queue.isEmpty()) {
            root = queue.poll();
            if(root == null) {
                if(parentCount*2 == childCount){
                    parentCount = 0;
                    childCount = 0;
                    if(!queue.isEmpty())
                        queue.add(null);
                    continue;
                }else{
                    parentCount = 0;
                    childCount = 0;
                    if(!queue.isEmpty())
                        queue.add(null);
                    continue;
                }
            }else{
                parentCount++;
            }
            if(root.left == null && root.right != null)
                return false;
            if(root.left != null) {
                queue.add(root.left);
                childCount++;
            }
            if(root.right != null) {
                queue.add(root.right);
                childCount++;
            }
        }
        return true;
    }
}
