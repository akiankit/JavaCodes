package com.structures.ds;

import java.util.ArrayList;

class TSTNode {
    public char data;

    public boolean isEnd;

    public TSTNode left, middle, right;
    
    public int count;

    public TSTNode(char data) {
        this.data = data;
        this.isEnd = false;
        this.left = null;
        this.middle = null;
        this.right = null;
    }
}

public class TernarySearchTree {
    public TSTNode root;

    private ArrayList<String> al;

    public TernarySearchTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }
    
    public void makeEmpty() {
        root = null;
    }

    public void insert(String word) {
        root = insert(root, word.toCharArray(), 0);
    }

    public TSTNode insert(TSTNode r, char[] word, int index) {
        if (r == null)
            r = new TSTNode(word[index]);

        if (word[index] < r.data)
            r.left = insert(r.left, word, index);
        else if (word[index] > r.data)
            r.right = insert(r.right, word, index);
        else {
            int lastIndex = word.length - 1;
            if (index != lastIndex)
                r.middle = insert(r.middle, word, index + 1);
            else {
                r.isEnd = true;
                r.count = r.count + 1;
            }
        }
        return r;
    }

    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }

    /** function to delete a word **/
    /** to delete a word just make isEnd false **/
    private void delete(TSTNode r, char[] word, int index) {
        if (r == null)
            return;

        if (word[index] < r.data)
            delete(r.left, word, index);
        else if (word[index] > r.data)
            delete(r.right, word, index);
        else {
            if (r.isEnd && index == word.length - 1){
                if(r.count == 1){
                    r.isEnd = false;
                    r.count = 0;
                }else{
                    r.count = r.count - 1;
                }
            }

            else if (index + 1 < word.length)
                delete(r.middle, word, index + 1);
        }
    }

    public boolean search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    private boolean search(TSTNode r, char[] word, int index) {
        if (r == null)
            return false;

        if (word[index] < r.data)
            return search(r.left, word, index);
        else if (word[index] > r.data)
            return search(r.right, word, index);
        else {
            if (index == word.length - 1)
                return r.isEnd;
            else
                return search(r.middle, word, index + 1);
        }
    }

    public String toString() {
        al = new ArrayList<String>();
        traverse(root, "");
        return "\nTernary Search Tree : " + al;
    }

    /** function to traverse tree **/
    // Traversal gives strings in sorted order that is the reason we are going first in
    // left,middle,right order.
    private void traverse(TSTNode r, String str) {
        if (r != null) {
            traverse(r.left, str);

            str = str + r.data;
            if (r.isEnd) {
                for (int i = 0; i < r.count; i++)
                    al.add(str);
            }

            traverse(r.middle, str);
            // if we are going in right direction that means current charcter is not part of string.
            // so we need to exclude it.
            str = str.substring(0, str.length() - 1);

            traverse(r.right, str);
        }
    }
}