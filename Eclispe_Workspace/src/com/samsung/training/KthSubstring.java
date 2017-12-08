package com.samsung.training;

import java.util.LinkedList;
import java.util.List;

public class KthSubstring {

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("d");
        trie.insert("od");
        trie.insert("ood");
        trie.insert("food");
        int count = 6;
        List<String> list = new LinkedList<String>();
        StringBuffer sb = new StringBuffer();
        for(int i=1;i<10;i++) {
            System.out.println("For " + i + "=");
            trie.traverse(trie.root,list,sb,i,0);
            System.out.println(list);
            list.clear();
        }
        
        
    }

}


class TrieNode {

    TrieNode[] nodes;

    boolean isEndOfWord;

    // Initialize your data structure here.
    public TrieNode() {
        nodes = new TrieNode[26];
        isEndOfWord = false;
    }

}

class Trie {
    public TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNode temp = root;
        for (int i = 0; i < word.length(); i++) {
            if (temp.nodes[word.charAt(i) - 'a'] == null) {
                temp.nodes[word.charAt(i) - 'a'] = new TrieNode();
            }
            if (i == word.length() - 1) {
                temp.nodes[word.charAt(i) - 'a'].isEndOfWord = true;
            }
            temp = temp.nodes[word.charAt(i) - 'a'];
        }
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        return search(word, 0, root);
    }

    private boolean search(String word, int i, TrieNode root) {
        if (i >= word.length())
            return false;
        if (root.nodes[word.charAt(i) - 'a'] == null)
            return false;
        else if (root.nodes[word.charAt(i) - 'a'].isEndOfWord == true && i == word.length() - 1)
            return true;
        else
            return search(word, i + 1, root.nodes[word.charAt(i) - 'a']);
    }

    private boolean startsWith(String prefix, int i, TrieNode root) {
        if (i >= prefix.length())
            return false;
        if (root.nodes[prefix.charAt(i) - 'a'] == null)
            return false;
        else if (i == prefix.length() - 1)
            return true;
        else
            return startsWith(prefix, i + 1, root.nodes[prefix.charAt(i) - 'a']);
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0, root);
    }
    
    /*public String toString() {
        List<String> al = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        traverse(root, al, sb,0,0);
        return "\nTrie : " + al;
    }*/
    
    /**
     * This method handles searching with '.' character
     * Like if bad and pad is inserted and if ".ad" or "b.." is searched
     * it should return true. 
     * @param word Pattern to be search in trie
     * @return true if word is found.
     */
    public boolean searchSpecial(String word) {
        return searchSpecial(word, 0, root);
    }
    
    private boolean searchSpecial(String word, int i, TrieNode root) {
        if (i >= word.length())
            return false;
        if (word.charAt(i) == '.') {
            TrieNode[] nodes = root.nodes;
            for (TrieNode node : nodes) {
                if (node != null) {
                    if (i == word.length() - 1 && node.isEndOfWord == true) {
                        return true;
                    }
                    boolean res = searchSpecial(word, i + 1, node);
                    if (res == true)
                        return true;
                }
            }
        } else {
            if (root.nodes[word.charAt(i) - 'a'] == null)
                return false;
            else if (root.nodes[word.charAt(i) - 'a'].isEndOfWord == true
                    && i == word.length() - 1)
                return true;
            else
                return searchSpecial(word, i + 1, root.nodes[word.charAt(i) - 'a']);
        }
        return false;
    }

    public void traverse(TrieNode root, List<String> al, StringBuffer word, int count, int temp) {
        if (root == null) {
            return;
        }
        TrieNode[] nodes = root.nodes;
        int tempCount = 0;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                tempCount++;
                if(count == temp+tempCount){
                    al.add(word.toString());
                    return;
                }
                char c = (char)(i + 'a');
                StringBuffer sb = new StringBuffer(word);
                traverse(nodes[i], al, sb.append(c),count,temp+tempCount);
            }
        }
    }
}