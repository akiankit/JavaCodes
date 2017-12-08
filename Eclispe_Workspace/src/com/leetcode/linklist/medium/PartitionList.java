/*Given a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.

You should preserve the original relative order of the nodes in each of the two partitions.

For example,
Given 1->4->3->2->5->2 and x = 3,
return 1->2->2->4->3->5.

Input:  {1,3,-1,5,2,1}, 3
Output: {1,1,2,-1,3,5}
Expected:   {1,-1,2,1,3,5}

 */
package com.leetcode.linklist.medium;

class ListNode {
    int val;

    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        ListNode temp = this;
        while (temp != null) {
            result = result.append(temp.val).append("->");
            temp = temp.next;
        }
        return result.toString();
    }

}

public class PartitionList {

    public static void main(String[] args) {
        int[] array = {
                // 1, 3, -1, 5, 2, 1
                // 2, 1
                1, 4, 3, 2, 5, 2
        };
        ListNode head = new ListNode(array[0]);
        ListNode temp = head;
        for(int i=1;i<array.length;i++){
            temp.next = new ListNode(array[i]);
            temp = temp.next;
        }
        temp = head;
        while (temp != null) {
            System.out.print(temp.val + "->");
            temp = temp.next;
        }
        System.out.println();
        System.out.println("Newlist");
        ListNode partition = partition(head, 3);
        while (partition != null) {
            System.out.print(partition.val + "->");
            partition = partition.next;
        }
    }

    public static ListNode partition(ListNode head, int x) {
        ListNode curr = head;
        ListNode prev = null;
        // seperate condition if head value is greater or equal to x
        while (curr != null && curr.val < x) {
            prev = curr;
            curr = curr.next;
        }
        ListNode temp = curr;
        ListNode prevToTemp = prev;
        while (temp != null) {
            if (temp.val < x) {
                if (curr == head) {
                    prevToTemp.next = temp.next;
                    temp.next = curr;
                    head = temp;
                    prev = head;
                    temp = curr;
                } else {
                    prev.next = temp;
                    prevToTemp.next = temp.next;
                    temp.next = curr;
                    prev = temp;
                    curr = prev.next;
                    temp = prevToTemp.next;
                }
            } else {
                prevToTemp = temp;
                temp = temp.next;
            }
        }
        return head;
    }
}
