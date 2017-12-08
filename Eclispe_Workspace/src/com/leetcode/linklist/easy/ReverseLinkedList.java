
package com.leetcode.linklist.easy;

public class ReverseLinkedList {

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

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode head = new ListNode(2);
        head.next = new ListNode(3);
        head.next.next = new ListNode(4);
        System.out.println(head);
        System.out.println(reverseList(head));
    }

    static public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode p = head;
        ListNode q = null;
        while (p != null) {
            ListNode temp = q;
            q = p;
            p = p.next;
            q.next = temp;
        }
        head = q;
        return head;
    }
    
    static public ListNode reverseListRecursive(ListNode head) {
        return null;
        /*
        if(head == null)
            return head;
        if(head.next == null)
            return head;
        ListNode p = head.next;
        ListNode q = head;
        head = p.next;
        p.next = q;
        return reverseListRecursive(head);
    */}
}
