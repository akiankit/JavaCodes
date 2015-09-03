package com.leetcode.linklist.medium;


public class RemoveDuplicatesfromSortedListII {

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
                sb.append(temp.val);
                if(temp.next != null)
                    sb.append("->");
                temp = temp.next;
            }
            return sb.toString();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(1);
        head.next.next = new ListNode(1);
        head.next.next.next = new ListNode(2);
        head.next.next.next.next = new ListNode(2);
        System.out.println(head);
        System.out.println(deleteDuplicates(head));
    }

    static public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode p = head, q = null;
        while (p != null) {
            if (p.next != null && p.val == p.next.val) {
                while (p.next != null && p.val == p.next.val) {
                    p = p.next;
                }
                p = p.next;
                if (p == null && q == null)
                    return null;
                else if (q == null) {
                    head = p;
                } else {
                    q.next = p;
                }
            } else {
                q = p;
                p = p.next;
            }
        }
        return head;
    }
}
