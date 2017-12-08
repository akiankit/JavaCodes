package com.learning.geeksforgeeks;

public class LRUCache {

	private int capacity;
	Node head = null;
	Node tail = null;
	private int size;

	public static void main(String[] args) {}

	public LRUCache(int capacity) {
		this.capacity = capacity;
	}

	public int get(int key) {
		if (head == null)
			return -1;
		if (head.key == key)
			return head.val;
		if (tail.key == key) {
			Node p = tail.prev;
			if (p != null)
				p.next = null;
			tail.prev = null;
			head.prev = tail;
			tail.next = head;
			head = tail;
			tail = p;
			return head.val;
		}
		Node p = head;
		while (p != null && p.val != key)
			p = p.next;
		if (p != null) {
			Node prevP = p.prev;
			Node nextP = p.next;
			head.prev = p;
			p.next = head;
			p.prev = null;
			head = p;
			if (prevP != null)
				prevP.next = nextP;
			if (nextP != null)
				nextP.prev = prevP;
			return head.val;
		}
		return -1;
	}

	public void set(int key, int value) {
		if (size == capacity) {
			Node p = tail.prev;
			tail.prev = null;
			if (p != null)
				p.next = null;
			tail = p;
			size--;
		}
		if (head == null || tail == null)
			head = tail = new Node(key, value);
		else {
			Node p = new Node(key, value);
			head.prev = p;
			p.next = head;
			head = p;
			if (tail == null)
				tail = head;
		}
		size++;
	}
}

class Node{
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
}