
package com.leetcode.strings.medium;

import java.util.ArrayList;
import java.util.List;

public class ImplementTrie {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Your Trie object will be instantiated and called as such:
        TrieArray trie = new TrieArray();
        trie.insert("ankit");
        trie.insert("ankita");
        trie.insert("ashish");
        trie.insert("ajith");
        trie.insert("avinash");
        trie.insert("arun");
        trie.insert("everything");
        trie.insert("nothing");
        System.out.println(trie.search("bc"));
        System.out.println(trie.startsWith("somet"));
        
        System.out.println(trie.toString());
    }

}

class TrieNodeArray {

    TrieNodeArray[] nodes;

    boolean isEndOfWord;

    // Initialize your data structure here.
    public TrieNodeArray() {
        nodes = new TrieNodeArray[26];
        isEndOfWord = false;
    }

}

class TrieArray {
    private TrieNodeArray root;

    public TrieArray() {
        root = new TrieNodeArray();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNodeArray temp = root;
        for (int i = 0; i < word.length(); i++) {
            if (temp.nodes[word.charAt(i) - 'a'] == null) {
                temp.nodes[word.charAt(i) - 'a'] = new TrieNodeArray();
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

    private boolean search(String word, int i, TrieNodeArray root) {
        if (i >= word.length())
            return false;
        if (root.nodes[word.charAt(i) - 'a'] == null)
            return false;
        else if (root.nodes[word.charAt(i) - 'a'].isEndOfWord == true && i == word.length() - 1)
            return true;
        else
            return search(word, i + 1, root.nodes[word.charAt(i) - 'a']);
    }

    private boolean startsWith(String prefix, int i, TrieNodeArray root) {
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
    
    /** function to print tree **/
    public String toString() {
        List<String> al = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        traverse(root, al, sb);
        return "\nTrie : " + al;
    }
    
    /**
     * This method handles searching with '.' character
     * Like if bad and pad is inserted and if ".ad" or "b.." is searched
     * it should return true. 
     * @param word Pattern to be search in trie
     * @return true if word is found.
     */
    public boolean searchSpecial(String word){
        return searchSpecial(word,0,root);
    }
    
    private boolean searchSpecial(String word, int i, TrieNodeArray root) {
        if (i >= word.length())
            return false;
        if (word.charAt(i) == '.') {
            TrieNodeArray[] nodes = root.nodes;
            for (TrieNodeArray node : nodes) {
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

    private void traverse(TrieNodeArray root, List<String> al, StringBuffer word) {
        if (root == null) {
            return;
        }
        TrieNodeArray[] nodes = root.nodes;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                char c = (char)(i + 'a');
                if (nodes[i].isEndOfWord == true) {
                    StringBuffer sb1 = new StringBuffer(word);
                    al.add(sb1.append(c).toString());
                }
                StringBuffer sb = new StringBuffer(word);
                traverse(nodes[i], al, sb.append(c));
            }
        }
    }
}