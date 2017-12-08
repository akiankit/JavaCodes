package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ManageActivities {

    static class CircularListNode {
        public CircularListNode(String string) {
            this.data = string;
            this.next = null;
        }

        String data;

        CircularListNode next;

        @Override
        public String toString() {
            return data + "";
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        CircularListNode head = null;
        CircularListNode tail = null;
        String[] strings = str.split(",");
        for (int i = 0; i < strings.length; i += 1) {
            if (head == null && tail == null) {
                head = tail = new CircularListNode(strings[i]);
                tail.next = head;
            } else {
                CircularListNode temp = new CircularListNode(strings[i]);
                temp.next = head;
                tail.next = temp;
                tail = temp;
            }
        }
        int count = strings.length;
        str = in.readLine();
        int N = Integer.parseInt(str);
        CircularListNode p = head;
        CircularListNode q = null;
        if (N == 1) {
            System.out.println(tail.data);
        } else {
            while (count > 1) {
                int i = 1;
                while (i < N) {
                    q = p;
                    p = p.next;
                    i++;
                }
                q.next = p.next;
                p = p.next;
                count--;
            }
            System.out.println(p.data);
        }

    }

}

