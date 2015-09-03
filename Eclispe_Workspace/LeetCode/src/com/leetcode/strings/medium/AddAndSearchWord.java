package com.leetcode.strings.medium;

import java.util.ArrayList;
import java.util.List;

public class AddAndSearchWord {

    public static void main(String[] args) {
      WordDictionary wordDictionary = new WordDictionary();
      wordDictionary.addWord("bad");
      wordDictionary.addWord("dad");
      wordDictionary.addWord("mad");
//      System.out.println(wordDictionary.search("pad"));
//      System.out.println(wordDictionary.search("bad"));
      System.out.println(wordDictionary.search("b.d"));
      System.out.println(wordDictionary.search("b.."));
      System.out.println(wordDictionary.search(".ad"));
    }

}

class WordDictionary {
    
    TrieArray trie;
    public WordDictionary() {
        trie = new TrieArray();
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
            return searchSpecial(word, 0, root);
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

    // Your Trie object will be instantiated and called as such:
    // Trie trie = new Trie();
    // trie.insert("somestring");
    // trie.search("key");

    // Adds a word into the data structure.
    public void addWord(String word) {
        trie.insert(word);
    }

    // Returns if the word is in the data structure. A word could
    // contain the dot character '.' to represent any one letter.
    public boolean search(String word) {
        return trie.search(word);
    }
}
