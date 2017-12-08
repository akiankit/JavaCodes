package com.leetcode.linklist.easy;

public class RemoveLinkedListElements {

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
            while(temp != null) {
                sb.append(temp.val).append("->");
                temp = temp.next;
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(6);
        head.next.next = new ListNode(6);
        head.next.next.next = new ListNode(6);
        head.next.next.next.next = new ListNode(1);
        System.out.println(head);
        System.out.println(removeElements(head, 6));
    }

    public static ListNode removeElements(ListNode head, int val) {
        if (head == null)
            return head;
        ListNode p = head;
        ListNode q = null;
        while (p != null) {
            if (p.val == val) {
                if (p == head) {//First element
                    p = p.next;
                    head = p;
                } else if (p.next == null) {//Last element
                    q.next = null;
                    p = null;
                } else {// In between element
                    q.next = p.next;
                    p = p.next;
                }
            } else {
                q = p;
                p = p.next;
            }
        }
        return head;
    }
}
