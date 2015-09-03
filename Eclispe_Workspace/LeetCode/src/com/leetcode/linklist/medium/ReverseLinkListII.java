package com.leetcode.linklist.medium;

public class ReverseLinkListII {
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
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        System.out.println(head);
        System.out.println(reverseBetween(head, 1,1));
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
            System.out.println(p.val);
            q = p.next;
            p = q.next;
            i++;
            System.out.println("p.val=" + p.val + " q.val=" + q.val);
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

}
