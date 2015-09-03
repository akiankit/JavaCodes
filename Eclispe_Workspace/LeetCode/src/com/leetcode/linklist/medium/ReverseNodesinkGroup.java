package com.leetcode.linklist.medium;


public class ReverseNodesinkGroup {
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

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        System.out.println(head);
        System.out.println(reverseKGroup(head, 2));
    }

    static public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || head.next == null || m == n)
            return head;
        if (m != 1) {
            int i = 1;
            ListNode p = head;
            ListNode q = null;
            while (i < m - 1) {
                p = p.next;
                i++;
            }
            ListNode reverseStartPoint = p;
//            System.out.println(p.val);
            q = p.next;
            p = q.next;
            i++;
//            System.out.println("p.val=" + p.val + " q.val=" + q.val);
            while (i < n && p != null) {
                ListNode temp = q;
                q = p;
                p = p.next;
                q.next = temp;
                i++;
            }
            ListNode temp = reverseStartPoint.next;
            reverseStartPoint.next = q;
            temp.next = p;
        } else {
            int i = 1;
            ListNode p = head;
            ListNode q = null;
            while (i <= n) {
                ListNode temp = q;
                q = p;
                p = p.next;
                q.next = temp;
                i++;
            }
            head.next = p;
            head = q;
        }
        return head;
    }
    
    static public ListNode reverseKGroup(ListNode head, int k) {
        int start = 1;
        int end = k;
        int size = getSizeOfList(head);
        if (head == null || head.next == null || k == 1 || k > size)
            return head;
        while (end <= size) {
            head = reverseBetween(head, start, end);
            start = start + k;
            end = end + k;
        }
        return head;
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
}
