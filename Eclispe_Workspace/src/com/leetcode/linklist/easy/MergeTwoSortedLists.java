package com.leetcode.linklist.easy;


public class MergeTwoSortedLists {

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
        headA.next = new ListNode(3);
//        headA.next.next = new ListNode(4);
//        headA.next.next.next = new ListNode(5);
//        headA.next.next.next.next = new ListNode(6);
        System.out.println(headA);

        ListNode headB = new ListNode(2);
//        headB.next = new ListNode(3);
//        headB.next.next = new ListNode(4);
//        headB.next.next.next = new ListNode(4);
        System.out.println(headB);
        System.out.println(mergeTwoLists(headA, headB));
    }

    static public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null)
            return l2;
        if (l2 == null)
            return l1;
        ListNode p1 = l1, p2 = l2, res = null, head = null;
        boolean firstTime = true;
        ListNode temp;
        while (p1 != null && p2 != null) {
            if (p1.val < p2.val) {
                temp = p1;
                p1 = p1.next;
            } else {
                temp = p2;
                p2 = p2.next;
            }
            if (firstTime) {
                firstTime = false;
                head = res = temp;
            } else {
                res.next = temp;
                res = temp;
            }
        }
        while (p1 != null) {
            temp = p1;
            p1 = p1.next;
            res.next = temp;
            res = temp;
        }
        while (p2 != null) {
            temp = p2;
            p2 = p2.next;
            res.next = temp;
            res = temp;
        }
        return head;
    }
}
