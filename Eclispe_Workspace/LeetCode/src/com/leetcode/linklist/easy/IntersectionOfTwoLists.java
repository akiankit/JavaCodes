package com.leetcode.linklist.easy;


public class IntersectionOfTwoLists {

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
        ListNode headA = new ListNode(2);
        headA.next = new ListNode(3);
        headA.next.next = new ListNode(4);
        headA.next.next.next = new ListNode(5);
        headA.next.next.next.next = new ListNode(6);
        System.out.println(headA);

        ListNode headB = new ListNode(2);
        headB.next = new ListNode(3);
        headB.next.next = new ListNode(4);
        headB.next.next.next = new ListNode(4);
        headB.next.next.next.next = headA.next.next.next;
        headB.next.next.next.next.next = headA.next.next.next.next;
        System.out.println(headB);
        System.out.println(getIntersectionNode(null, headB));
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

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int c1 = getSizeOfList(headA);
        int c2 = getSizeOfList(headB);
        int diff = Math.abs(c1-c2);
        ListNode p1 = headA;
        ListNode p2 = headB;
        if (c1 > c2) {
            int i = 0;
            while (i < diff) {
                p1 = p1.next;
                i++;
            }
        } else if (c2 > c1) {
            int i = 0;
            while (i < diff) {
                p2 = p2.next;
                i++;
            }
        }
        while(p1 != p2 && p1 != null && p2 != null) {
            p1 = p1.next;
            p2 = p2.next;
        }
        if (p1 != null && p2 != null && p1 == p2)
            return p1;
        else return null;
    }
}
