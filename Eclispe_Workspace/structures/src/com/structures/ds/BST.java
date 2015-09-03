
package com.structures.ds;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BST {

    BSTNode root;
    
    public boolean findAncestorPath(List<Integer> ancestors, BSTNode node, int number) {
        if (node == null)
            return false;

        int data = node.val;
        if (data == number)
            return true;

        if (findAncestorPath(ancestors, node.left, number)) {
            ancestors.add(data);
            return true;
        }

        if (findAncestorPath(ancestors, node.right, number)) {
            ancestors.add(data);
            return true;
        }

        return false;
    }

    public BSTNode find(int n) {
        if (root == null)
            return null;
        BSTNode temp = root;
        while (true) {
            if (n == temp.val)
                return temp;
            if (n > temp.val) {
                if (temp.right != null)
                    temp = temp.right;
                else
                    return null;
            } else {
                if (temp.left != null)
                    temp = temp.left;
                else
                    return null;
            }
        }
    }

    public BSTNode getMin(BSTNode node) {
        if (node == null)
            return null;
        if (node != null && node.left == null)
            return node;
        return getMin(node.left);
    }

    public void addArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            addVal(arr[i]);
        }
    }

    public void addVal(int n) {
        addNode(new BSTNode(n));
    }

    public void addNode(BSTNode node) {
        if (root == null) {
            root = node;
            return;
        }
        BSTNode temp = root;
        while (true) {
            if (node.val > temp.val) {
                if (temp.right == null) {
                    temp.right = node;
                    return;
                }
                temp = temp.right;
            } else {
                if (temp.left == null) {
                    temp.left = node;
                    return;
                }
                temp = temp.left;
            }
        }
    }

    public BSTNode getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public static void main(String[] args) {
        int[] a = {
                4, 3, 4, 5, 2, 6, 7, 2, 1
        };
        BST bst = new BST();
        bst.addArray(a);
        int[] ar = null;
        LinkedList<Integer> list = new LinkedList<Integer>();
        bst.findAncestorPath(list, bst.root, 7);
        System.out.println(list);
        ar = bst.inOrderTraversal();
        System.out.println(Arrays.toString(ar));
        /*// System.out.println("hello");
        bst.delete(bst.root, 6);
        ar = bst.getSorted();
        // bst.inOrderTraversal(ar);
        System.out.println(Arrays.toString(ar));
        System.out.println("hello");*/
    }

    public int[] getSorted() {
        return inOrderTraversal();
    }

    public int getSizeOfTree(BSTNode node) {
        if (node == null)
            return 0;
        if (node.left == null && node.right == null)
            return 1;
        else
            return 1 + getSizeOfTree(node.left) + getSizeOfTree(node.right);
    }

    public BSTNode delete(BSTNode root, int n) {
        // base case
        if (root == null)
            return root;
        if (n < root.val) {
            root.left = delete(root.left, n);
        } else if (n > root.val) {
            root.right = delete(root.right, n);
        } else {
            // Root value is equal to node value;
            if (root.left == null) {
                root = root.right;
                return root;
            } else if (root.right == null) {
                root = root.left;
                return root;
            }
            // Both child are not null
            // Reaching here denotes that node has two child so we need to find inorder successor
            // and
            // then replace node with successor.
            // http://en.wikipedia.org/wiki/Binary_search_tree#Deletion
            BSTNode min = getMin(root.right);
            root.val = min.val;
            root.right = delete(root.right, min.val);
        }
        return root;
    }

    // http://www.geeksforgeeks.org/inorder-successor-in-binary-search-tree/
    public BSTNode inorderSuccessor(BSTNode node) {
        if (node == null)
            return null;
        if (node.right != null)
            return getMin(node);
        BSTNode temp = root;
        BSTNode succ = null;
        while (temp != null) {
            if (temp.val < node.val) {
                temp = temp.right;
            } else if (temp.val > node.val) {
                succ = temp;
                temp = temp.left;
            } else
                break;
        }
        return succ;
    }

    public int[] inOrderTraversal() {
        int size = getSizeOfTree(root);
        int[] inOrder = new int[size];
        int index = 0;
        if (root == null)
            return null;
        Stack<BSTNode> stack = new Stack<BSTNode>();
        stack.push(root);
        BSTNode temp = root;
        temp = root.left;
        while (!stack.empty()) {
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
            if (temp == null && !stack.empty()) {
                temp = stack.pop();
                inOrder[index] = temp.val;
                // System.out.print(temp.val+",");
                index++;
                temp = temp.right;
            }
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
        }
        // System.out.println(Arrays.toString(inOrder));
        return inOrder;
    }
}
