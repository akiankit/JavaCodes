/*3,9,20,#,#,15,7*/

package com.leetcode;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.leetcode.util.TreeNode;

public class BinaryTreeTraversal {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.left.right.right = new TreeNode(6);
//        root.right.left = new TreeNode(6);
//        root.right.right = new TreeNode(7);
//        root.right.left.left = new TreeNode(7);
//        root.right.right = new TreeNode(4);
//        root.right.right.left = new TreeNode(6);
//        root.right.right.left.left = new TreeNode(8);
//        root.right.right.left.right = new TreeNode(9);
        System.out.println(postorderTraversal(root));
//        System.out.println(reverseInorderTraversal(root));
//        System.out.println(preorderTraversal(root));
//        System.out.println(postorderTraversal(root));
    }
    /*1.1 Create an empty stack
    2.1 Do following while root is not NULL
        a) Push root's right child and then root to stack.
        b) Set root as root's left child.
    2.2 Pop an item from stack and set it as root.
        a) If the popped item has a right child and the right child
           is at top of stack, then remove the right child from stack,
           push the root back and set root as root's right child.
        b) Else print root's data and set root as NULL.
    2.3 Repeat steps 2.1 and 2.2 while stack is not empty.*/
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new LinkedList<Integer>();
        Stack<TreeNode> stack = new Stack<TreeNode>();
        if (root == null)
            return result;
        else {
            while (true) {
                while (root != null) {
                    if (root.right != null){
                        stack.push(root.right);
                        System.out.println("Push="+stack);
                    }
                    stack.push(root);
                    System.out.println("Push="+stack);
                    root = root.left;
                }
                if (stack.isEmpty())
                    break;
                root = stack.pop();
                System.out.println("pop="+stack);
                if (root.right != null && stack.isEmpty() == false && stack.peek() == root.right) {
                    stack.pop();
                    System.out.println("pop="+stack);
                    stack.push(root);
                    root = root.right;
                    System.out.println("Push="+stack);
                } else {
                    result.add(root.val);
                    System.out.println("result="+result);
                    root = null;
                }
            }
        }
        return result;
    }

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new LinkedList<Integer>();
        Stack<TreeNode> stack = new Stack<TreeNode>();
        if (root == null)
            return result;
        else {
            while (true) {
                while (root != null) {
                    stack.push(root);
                    System.out.println("push="+stack);
                    root = root.left;
                }
                if (stack.isEmpty() == true) {
                    break;
                }
                root = stack.pop();
                System.out.println("After Pop="+stack);
                result.add(root.val);
                System.out.println("result="+result);
                root = root.right;
            }
        }
        return result;
    }
    
    public static List<Integer> reverseInorderTraversal(TreeNode root) {
        List<Integer> result = new LinkedList<Integer>();
        Stack<TreeNode> stack = new Stack<TreeNode>();
        if (root == null)
            return result;
        else {
            while (true) {
                while (root != null) {
                    stack.push(root);
                    root = root.right;
                }
                if (stack.isEmpty() == true) {
                    break;
                }
                root = stack.pop();
                result.add(root.val);
                root = root.left;
            }
        }
        return result;
    }
    
    

    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new LinkedList<Integer>();
        Stack<TreeNode> stack = new Stack<TreeNode>();
        if (root == null)
            return result;
        else {
            while (true) {
                while (root != null) {
                    result.add(root.val);
                    stack.push(root);
                    System.out.println("push="+stack);
                    System.out.println("result="+result);
                    root = root.left;
                }
                if (stack.isEmpty() == true) {
                    break;
                }
                root = stack.pop();
                System.out.println("After Pop="+stack);
                root = root.right;
            }
        }
        return result;
    }

}
