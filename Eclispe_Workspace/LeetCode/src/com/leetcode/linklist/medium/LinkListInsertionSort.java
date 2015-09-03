package com.leetcode.linklist.medium;

public class LinkListInsertionSort {

    static class ListNode {
        int val;

        ListNode next;

        ListNode(int x) {
            val = x;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            ListNode temp = this;
            while (temp != null) {
                sb.append(temp.val);
                if (temp.next != null)
                    sb.append("->");
                temp = temp.next;
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        ListNode headA = new ListNode(1);
        headA.next = new ListNode(2);
        headA.next.next = new ListNode(1);
        headA.next.next.next = new ListNode(5);
        headA.next.next.next.next = new ListNode(3);
        System.out.println(headA);
        System.out.println(insertionSortList(null));
    }

    static public ListNode insertionSortList(ListNode head) {
        if(head == null || head.next == null)
            return head;
        ListNode newHead = new ListNode(head.val);
        ListNode q = head;
        ListNode p = head.next;
        while(p != null) {
            ListNode temp = p;
            q.next = p.next;
            temp.next = null;
            newHead = insertSorted(newHead,temp);
            p = q.next;
        }
        return newHead;
    }

    private static ListNode insertSorted(ListNode newHead, ListNode node) {
        ListNode p = newHead;
        ListNode q = null;
        while (p != null && p.val < node.val) {
            q = p;
            p = p.next;
        }
        if (q == null) {
            node.next = newHead;
            newHead = node;
        } else if (p == null) {
            q.next = node;
        } else {
            q.next = node;
            node.next = p;
        }
        return newHead;
    }
}
