package com.leetcode.linklist.medium;

import com.leetcode.util.TreeNode;

public class ConvertSortedListToBinarySearchTree {
    
    public static void main(String[] args) {
        int[] array = {
                // 1, 3, -1, 5, 2, 1
                // 2, 1
                1,2,3,4,5,6,7
        };
        ListNode head = new ListNode(array[0]);
        ListNode temp = head;
        for(int i=1;i<array.length;i++){
            temp.next = new ListNode(array[i]);
            temp = temp.next;
        }
        ConvertSortedListToBinarySearchTree cs = new ConvertSortedListToBinarySearchTree();
        TreeNode root = cs.sortedListToBSTRecur(head);
        System.out.println(root);
    }

    public TreeNode sortedListToBSTRecur(ListNode head) {
        if(head == null)
            return null;
        if(head.next == null) 
            return new TreeNode(head.val);
        if(head.next.next == null) {
            TreeNode root = new TreeNode(head.val);
            root.right = new TreeNode(head.next.val);
            return root;
        }
        int  n = countNodes(head);
        return sortedListToBSTRecurUtil(head, n);
    }

    // http://www.geeksforgeeks.org/sorted-linked-list-to-balanced-bst/
    private TreeNode sortedListToBSTRecurUtil(ListNode head, int n) {
        if(n <= 0)
            return null;
        TreeNode left = sortedListToBSTRecurUtil(head, n/2);
        TreeNode root = new TreeNode(head.val);
        root.left = left;
        head = head.next;
        root.right = sortedListToBSTRecurUtil(head, n-n/2-1);
        return root;
    }

    private int countNodes(ListNode head) {
        if(head == null)
            return 0;
        int count = 0;
        while(head!= null){
            head = head.next;
            count++;
        }
        return count;
    }

    public TreeNode sortedListToBST(ListNode head) {
        if(head == null)
            return null;
        if(head.next == null) 
            return new TreeNode(head.val);
        if(head.next.next == null) {
            TreeNode root = new TreeNode(head.val);
            root.right = new TreeNode(head.next.val);
            return root;
        }
        ListNode[] midList = getMiddleOfList(head);
        TreeNode root = new TreeNode(midList[0].val);
        if(midList[1] != null)
            midList[1].next = null;
        root.left = sortedListToBST(head);
        root.right = sortedListToBST(midList[0].next);
        return root;
    }
    
    public ListNode[] getMiddleOfList(ListNode head) {
        if (head == null || head.next == null)
            return null;
        ListNode slow = head;
        ListNode fast = head.next.next;
        ListNode[] res = new ListNode[2];
        ListNode slowPrev = null;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slowPrev = slow;
            slow = slow.next;
        }
        if (fast == null) {
            res[0] = slow;
            res[1] = slowPrev;
        } else if (fast.next == null) {
            res[0] = slow.next;
            res[1] = slow;
        }
            
        return res;
    }
}
