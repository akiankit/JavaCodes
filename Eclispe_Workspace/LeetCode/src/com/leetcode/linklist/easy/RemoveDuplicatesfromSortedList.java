package com.leetcode.linklist.easy;


public class RemoveDuplicatesfromSortedList {

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
        ListNode headA = new ListNode(2);
        headA.next = new ListNode(4);
        headA.next.next = new ListNode(4);
        headA.next.next.next = new ListNode(4);
        headA.next.next.next.next = new ListNode(5);
        System.out.println(headA);
        System.out.println(deleteDuplicates(headA));
    }

    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode p = head.next;
        ListNode q = head;
        while (p != null) {
            if (p.val == q.val) {
                q.next = p.next;
                p = q.next;
            } else {
                q = p;
                p = p.next;
            }
        }
        return head;
    }
}
