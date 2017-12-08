package com.leetcode.linklist.medium;



public class LinkedListCycle {

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
        headA.next.next.next.next.next = headA.next;
//        System.out.println(headA);
        System.out.println(cycleNode(headA).val);
    }

    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null)
            return false;
        if (head.next == head)
            return true;
        ListNode slow = head;
        ListNode fast = head.next.next;
        while (fast != null && fast.next != null && slow != null && fast != slow) {
            fast = fast.next.next;
            slow = slow.next;
        }
        if (fast == null || slow == null)
            return false;
        if (fast != null && slow != null && fast == slow)
            return true;
        return false;
    }
    
    //-21,10,17,8,4,26,5,35,33,-7,-16,27,-12,6,29,-12,5,9,20,14,14,2,13,-24,21,23,-21,5
    public static ListNode cycleNode(ListNode head) {
        if (head == null || head.next == null)
            return null;
        if (head.next == head){
            return head;
        }
        ListNode slow = head;
        ListNode fast = head.next.next;
        while (fast != null && fast.next != null && slow != null && fast != slow) {
            fast = fast.next.next;
            slow = slow.next;
        }
        if (fast == null || slow == null)
            return null;
        if (fast != null && slow != null && fast == slow) {
            ListNode nodeInCycle = fast;
            slow = slow.next;
            int nodesInLoop = 1;
            System.out.println("node in cycle=" + fast.val);
            //Count No. of nodes in cycle.
            while (slow != nodeInCycle) {
                slow = slow.next;
                nodesInLoop++;
            }
            System.out.println("node in Loop=" + nodesInLoop);
            fast = nodeInCycle;
            slow = head;
            System.out.println("fast=" + fast.val);
            System.out.println("slow=" + slow.val);
            int i = 0;
            fast = head;
            //Fix one pointer at head and another at Kth No. of node of from head
            while (i < nodesInLoop) {
                i++;
                slow = slow.next;
            }
            //Start moving both pointers unless both are same.
            // Same point is start of loop.
            System.out.println("After incrementing slow=" + slow.val);
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        }
        return null;
    }
}
