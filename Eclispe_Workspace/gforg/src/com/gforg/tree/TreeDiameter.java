package com.gforg.tree;

import com.structures.ds.BSTNode;

public class TreeDiameter {

    // Diameter of a Binary Tree
    // The diameter of a tree (sometimes called the width) is the number of nodes on the longest
    // path between two leaves in the tree. The diagram below shows two trees each with diameter
    // nine, the leaves that form the ends of a longest path are shaded (note that there is more
    // than one path in each tree of length nine, but no path longer than nine nodes).
    /* Constructed binary tree is 
    1
  /   \
2      3
/  \
4     5
*/
    public static void main(String[] args) {
        BSTNode node = new BSTNode(1);
        node.left = new BSTNode(2);
        node.left.left = new BSTNode(4);
        node.left.right = new BSTNode(5);
        node.right = new BSTNode(3);
        System.out.println(diameter(node));
        System.out.println(getDiameter(node)[0]);
    }

    // The diameter of a tree T is the largest of the following quantities:
    // * the diameter of T’s left subtree
    // * the diameter of T’s right subtree
    // * the longest path between leaves that goes through the root of T (this can be computed from
    // the heights of the subtrees of T)
    public static int diameter(BSTNode root) {
        if (root == null)
            return 0;
        int leftHeight = BSTNode.height(root.left);
        int rightHeight = BSTNode.height(root.right);
        return Math.max(leftHeight + rightHeight + 1,
                Math.max(diameter(root.left), diameter(root.right)));
    }
    
    public static int[] getDiameter(BSTNode root) {
        int[] result = new int[] {
                0, 0
        }; // 1st element: diameter, 2nd: height
        if (root == null)
            return result;
        int[] leftResult = getDiameter(root.left);
        int[] rightResult = getDiameter(root.right);
        int height = Math.max(leftResult[1], rightResult[1]) + 1;
        int rootDiameter = leftResult[1] + rightResult[1] + 1;
        int leftDiameter = leftResult[0];
        int rightDiameter = rightResult[0];
        result[0] = Math.max(rootDiameter, Math.max(leftDiameter, rightDiameter));
        result[1] = height;
        return result;
    }
}
