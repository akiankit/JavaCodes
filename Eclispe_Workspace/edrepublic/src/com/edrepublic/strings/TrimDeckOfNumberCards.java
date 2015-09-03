package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/*
 Test Case: 1
Status: Passed
Input:
MMMMMMMMMM

Program Output: 0

Expected Output: 0
----------------
Test Case: 2
Status: Passed
Input:
MMMMMBRU23

Program Output: 1

Expected Output: 1
----------------
Test Case: 3
Status: Passed
Input:
MMMMBUR23L

Program Output: 6

Expected Output: 6
----------------
Test Case: 4
Status: Failed
Input:
BU68482BM6875RL5M9573R

Program Output: 18

Expected Output: 4
----------------
Test Case: 5
Status: Failed
Input:
BRM262362UMMBR6262437892MUULB332

Program Output: 10

Expected Output: 24
----------------
Test Case: 6
Status: Failed
Input:
MUR55662LL3MBBMM2235RMMBM253525MMLMMU

Program Output: 14

Expected Output: 22
----------------
Test Case: 7
Status: Failed
Input:
L92L43MU5U879RB2RM3

Program Output: 11

Expected Output: 9
----------------
Test Case: 8
Status: Failed
Input:
7879L4LRM24R46M2B3UR7U4256632UR738LB6MB8M959L5U895

Program Output: 37

Expected Output: 42
----------------
Test Case: 9
Status: Failed
Input:
B8929LUBR6L247759UMM5M3M947LRB284R4256R36587U6833B

Program Output: 0

Expected Output: 30
----------------
Test Case: 10
Status: Failed
Input:
BLLUMM29U883L2LLU9329LBLLULMM2LULMRR88UL2B

Program Output: 22

Expected Output: 31
*/
public class TrimDeckOfNumberCards {
    
    static class CircularListNode {
        public CircularListNode(char data) {
            this.data = data;
            this.next = null;
        }

        char data;

        CircularListNode next;

        @Override
        public String toString() {
            CircularListNode head = this;
            StringBuilder sb = new StringBuilder();
            sb.append(head.data + "->");
            head = head.next;
            while (head != this) {
                sb.append(head.data + "->");
                head = head.next;
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('B', 1);
        map.put('U', 10);
        map.put('L', 11);
        map.put('R', 12);
        map.put('M', 13);
        for(int i=2;i<=9;i++){
            map.put((char)(i + '0'), i);
        }
        System.out.println(map);

        String str = in.readLine();
        CircularListNode head = null;
        CircularListNode tail = null;
        for (int i = 0; i < str.length(); i++) {
            if (head == null) {
                head = tail = new CircularListNode(str.charAt(i));
            } else {
                CircularListNode temp = new CircularListNode(str.charAt(i));
                temp.next = head;
                tail.next = temp;
                tail = temp;
            }
        }
        System.out.println(head);
        int count = str.length();
        int THIRTEEN = 13;
        CircularListNode p = head.next;
        CircularListNode q = head;
        while (p != head) {
            if (map.get(p.data) == THIRTEEN) {
                if (p == tail) {
                    q.next = head;
                    tail = q;
                } else {
                    q.next = p.next;
                    p = p.next;
                }
                count--;
            } else {
                q = p;
                p = p.next;
            }
        }
        if (map.get(head.data) == THIRTEEN) {
            if (tail == head) {
                head = tail = null;
                count = 0;
            } else {
                tail.next = head.next;
                head = head.next;
                count--;
            }
        }
        if (count == 0) {
            System.out.println(count);
            return;
        }
        q = head;
        p = head;
        int tempCount = 0;
        System.out.println(head);
        while (true) {
            if (p == head) {
                tempCount = 1;
            }
            int sum = 0;
            int visited = 0;
            List<CircularListNode> list = new LinkedList<CircularListNode>();
            CircularListNode tempP = p;
            while (sum < 13 && visited < count) {
                sum = sum + map.get(tempP.data);
                list.add(tempP);
                tempP = tempP.next;
                visited++;
            }
            if (sum == 13) {
                System.out.println("Removing node=" + list.get(0).data + "To="
                        + list.get(list.size() - 1).data);
                for (CircularListNode node : list) {
                    p = node.next;
                    CircularListNode[] nodes = deleteNode(head, tail, node);
                    head = nodes[0];
                    tail = nodes[1];
                    count--;
                }
                System.out.println(head);
                //p = head;
            } else {
                p = p.next;
            }
            if(head == null || count ==0){
                System.out.println("0");
                return;
            }
//            System.out.println(head);
        }
//        System.out.println(count);
    }
    
    private static CircularListNode[] deleteNode(CircularListNode head, CircularListNode tail,
            CircularListNode p) {
        CircularListNode p1 = head;
        CircularListNode q1 = null;
        while(p != p1){
            q1 = p1;
            p1 = p1.next;
        }
        return deleteNode(head, tail, q1,p1);
    }
    
    private static CircularListNode[] deleteNode(CircularListNode head, CircularListNode tail,
            CircularListNode q, CircularListNode p) {
        CircularListNode[] nodes = new CircularListNode[2];
        if (p == head) {
            if (head == tail) {
                head = tail = null;
            } else {
                tail.next = head.next;
                head = head.next;
            }
        } else if (p == tail) {
            q.next = head;
            tail = q;
        } else {
            q.next = p.next;
            p = p.next;
        }
        nodes[0] = head;
        nodes[1] = tail;
        return nodes;
    }

}
