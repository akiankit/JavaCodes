package com.structures.ds;

import java.util.HashMap;
import java.util.Map;


public class LRUCache {
    
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(10);
        cache.set(10, 13);
        cache.set(3, 17);
        cache.set(6, 11);
        cache.set(10, 5);
        cache.set(9, 10);
        cache.get(13);
        cache.set(2, 19);
        cache.get(2);
        cache.get(3);
        cache.set(5, 25);
        cache.get(8);
        cache.set(9, 22);
        cache.set(5, 5);
        cache.set(1, 30);
        cache.get(11);
        cache.set(9, 12);
        cache.get(7);
        cache.get(5);
        cache.get(8);
        cache.get(9);
        cache.set(4, 30);
        cache.set(9, 3);
        cache.get(9);
        cache.get(10);
        cache.get(10);
        cache.set(6, 14);
        cache.set(3, 1);
        cache.get(3);
        cache.set(10, 11);
        cache.get(8);
        cache.set(2, 14);
        cache.get(1);
        cache.get(5);
        cache.get(4);
        cache.set(11, 4);
        cache.set(12, 24);
        cache.set(5, 18);
        cache.get(13);
        cache.set(7, 23);
        cache.get(8);
        cache.get(12);
        cache.set(3, 27);
        cache.set(2, 12);
        cache.get(5);
        cache.set(2, 9);
        cache.set(13, 4);
        cache.set(8, 18);
        cache.set(1, 7);
        cache.get(6);
        /*cache.set(9, 29);
        cache.set(8, 21);
        cache.get(5);
        cache.set(6, 30);
        cache.set(1, 12);
        cache.get(10);
        cache.set(4, 15);
        cache.set(7, 22);
        cache.set(11, 26);
        cache.set(8, 17);
        cache.set(9, 29);
        cache.get(5);
        cache.set(3, 4);
        cache.set(11, 30);
        cache.get(12);
        cache.set(4, 29);
        cache.get(3);
        cache.get(9);
        cache.get(6);
        cache.set(3, 4);
        cache.get(1);
        cache.get(10);
        cache.set(3, 29);
        cache.set(10, 28);
        cache.set(1, 20);
        cache.set(11, 13);
        cache.get(3);
        cache.set(3, 12);
        cache.set(3, 8);
        cache.set(10, 9);
        cache.set(3, 26);
        cache.get(8);
        cache.get(7);
        cache.get(5);
        cache.set(13, 17);
        cache.set(2, 27);
        cache.set(11, 15);
        cache.get(12);
        cache.set(9, 19);
        cache.set(2, 15);
        cache.set(3, 16);
        cache.get(1);
        cache.set(12, 17);
        cache.set(9, 1);
        cache.set(6, 19);
        cache.get(4);
        cache.get(5);
        cache.get(5);
        cache.set(8, 1);
        cache.set(11, 7);
        cache.set(5, 2);
        cache.set(9, 28);
        cache.get(1);
        cache.set(2, 2);
        cache.set(7, 4);
        cache.set(4, 22);
        cache.set(7, 24);
        cache.set(9, 26);
        cache.set(13, 28);
        cache.set(11, 26);*/
    }
    

    Node head;
    Node tail;
    
    public void removeTail() {
        Node p = tail.prev;
//        System.out.println("Removed Node, with key="+p.key +" value= "+p.val);
        map.remove(p.key);
        p.prev.next = tail;
        tail.prev = p.prev;
    }

    public void removeNode(Node node) {
        map.remove(node.key);
        Node p = node.prev;
        Node q = node.next;
        p.next = q;
        q.prev = p;
    }

    public void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }
    
    public void addToHead(Node node) {
        Node p = head.next;
        node.next = p;
        node.prev = head;
        head.next = node;
        p.prev = node;
        map.put(node.key, node);
    }

    private int capacity;
    private int size;
    private Map<Integer,Node> map;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<Integer,Node>();
        head = new Node(0,0);
        tail = new Node(0,0);
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
//        System.out.println(map);
        Node node = map.get(key);
        if (node != null) {
            moveToHead(node);
//            System.out.println("get("+key+")= "+node.val);
//            System.out.println(head.next);
//            System.out.println();
            return node.val;
        }
//        System.out.println("get(" + key + ")= " + "-1");
//        System.out.println();
        return -1;
    }
    
    public void set(int key, int value) {
        Node node = map.get(key);
        if (node != null) {
            map.remove(key);
            node.val = value;
            moveToHead(node);
            map.put(key, node);
        } else {
            if (size == capacity) {
                removeTail();
                size--;
            }
            node = new Node(key, value);
            map.put(key,node);
            addToHead(node);
            size++;
        }
//        System.out.println("set(" + key + "," + value + ") size=" + size);
//        System.out.println(head.next);
//        System.out.println();
        //        System.out.println(map);
    }
}

class Node {
    int key;

    int val;

    Node prev;

    Node next;

    public Node(int key, int val) {
        this.key = key;
        this.val = val;
        prev = null;
        next = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node temp = this;
        while (temp.next != null) {
            sb.append("[" + temp.key + "=" + temp.val + "]->");
            temp = temp.next;
        }
        return sb.toString();
    }
}