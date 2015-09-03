package com.leetcode.linklist.medium;

public class MergeKSortedList {

    static class ListNode {
        int val;

        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

        /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode[] lists = new ListNode[10];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new ListNode((int)Math.random());
        }
        mergeKLists(lists);
    }
    
    public static ListNode mergeKLists(ListNode[] lists) {
        int j = 1;
        while (j < lists.length) {
            for (int i = 0; i < lists.length; i += 2*j) {
                int index1 = i;
                int index2 = i + j;
                if(index1 != index2 && index2 < lists.length){
                    //System.out.println("Merging list "+index1+" "+index2);
                    lists[index1] = mergeTwoLists(lists[index1], lists[index2]);
                }
            }
            j = j << 1;
        }
        return lists[0];
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
        if (p1 != null)
            res.next = p1;
        if (p2 != null)
            res.next = p2;
        return head;
    }

}
