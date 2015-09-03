package com.leetcode.linklist.medium;

public class MergeSortList {

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
        System.out.println(sortList(headA));
    }

    static public ListNode sortList(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode f = head.next.next;
        ListNode p = head;
        //Finding middle node to divide into two parts.
        // It can be extracted into a function to make code more readable.
        while (f != null && f.next != null) {
            p = p.next;
            f =  f.next.next;
        }
        // After this step h2(second half) will be sorted
        ListNode h2 = sortList(p.next);
        p.next = null;
        // Now merging willl be done with sorting first half 
        return mergeTwoLists(sortList(head), h2);
    }

/*    private static ListNode sortListUtil(ListNode head, ListNode tail) {
        if (head == null || head.next == null || head == tail)
            return head;
        ListNode mid = getMidNode(head,tail);
        ListNode l1 = sortListUtil(head, mid);
        ListNode l2 = null;
        if (mid != null)
            l2 = sortListUtil(mid.next, tail);
        tail = mid;
        tail.next = null;
        return mergeTwoLists(l1, l2);
    }
    
    private static ListNode getMidNode(ListNode head, ListNode mid) {
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null && fast != mid && fast.next != mid) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
*/
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
        if (p1 != null)
            res.next = p1;
        if (p2 != null)
            res.next = p2;
        return head;
    }
}
