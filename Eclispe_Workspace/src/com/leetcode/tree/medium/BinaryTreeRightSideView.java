
package com.leetcode.tree.medium;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.leetcode.util.TreeNode;

public class BinaryTreeRightSideView {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(4);
        System.out.println(rightSideView(root));
    }

    public static List<Integer> rightSideView(TreeNode root) {
        if (root == null)
            return new LinkedList<Integer>();
        else {
            List<Integer> result = new LinkedList<Integer>();
            Queue<TreeNode> temp = new LinkedList<TreeNode>();
            int lastVal = root.val;
            result.add(lastVal);
            temp.add(root);
            temp.add(null);
            while (temp.isEmpty() == false) {
                TreeNode tempNode = temp.poll();
                if (tempNode == null && temp.isEmpty() == false) {
                    result.add(lastVal);
                    temp.add(null);
                    continue;
                }

                if (tempNode != null && tempNode.left != null) {
                    lastVal = tempNode.left.val;
                    temp.add(tempNode.left);
                }
                if (tempNode != null && tempNode.right != null) {
                    lastVal = tempNode.right.val;
                    temp.add(tempNode.right);
                }

            }
            return result;
        }
    }
}
