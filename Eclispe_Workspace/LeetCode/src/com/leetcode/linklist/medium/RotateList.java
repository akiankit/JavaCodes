package com.leetcode.linklist.medium;


public class RotateList {

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
        headA.next.next = new ListNode(3);
        headA.next.next.next = new ListNode(4);
        headA.next.next.next.next = new ListNode(5);
        System.out.println(headA);
        System.out.println(rotateRight(headA, 7));
    }

    private static int getSizeOfList(ListNode head) {
        ListNode temp = head;
        int count = 0;
        while(temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }
 
    static public ListNode rotateRight(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null)
            return head;
        int sizeOfList = getSizeOfList(head);
        k = k % sizeOfList;
        ListNode p = head;
        int i = 0;
        while(i<k) {
            i++;
            p = p.next;
        }
        ListNode q = head;
        while(p.next != null) {
            p = p.next;
            q = q.next;
        }
        p.next = head;
        head = q.next;
        q.next = null;
        return head;
    }
}
