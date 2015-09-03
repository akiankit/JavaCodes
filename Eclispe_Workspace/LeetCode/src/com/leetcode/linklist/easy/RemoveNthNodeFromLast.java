package com.leetcode.linklist.easy;


public class RemoveNthNodeFromLast {

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
        ListNode head = new ListNode(2);
        head.next = new ListNode(3);
        head.next.next = new ListNode(4);
        head.next.next.next = new ListNode(5);
        head.next.next.next.next = new ListNode(6);
        System.out.println(head);
        System.out.println(removeNthFromEnd(head, 5));
        
    }

    static public ListNode removeNthFromEnd(ListNode head, int n) {
        if(head == null)
            return head;
        int size = getSizeOfList(head);
        if (n < 1 || n > size)
            return head;
        int i = 1;
        ListNode p = head;
        ListNode q = head;
        if(size == n) {
            return head.next;
        }
        while(i<=n) {
            p = p.next;
            i++;
        }
        while(p.next != null) {
            p = p.next;
            q = q.next;
        }
//        System.out.println("q="+q);
//        System.out.println("p="+p);
        q.next = q.next.next;
        return head;
    }
    
    /*private static void delete(ListNode q, ListNode p) {
        q.next = p.next;
        p = null;
    }*/

    private static int getSizeOfList(ListNode head) {
        ListNode temp = head;
        int count = 0;
        while(temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }
}
