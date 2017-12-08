package com.leetcode.tree.medium;

public class PopulatingNextRightPointersinEachNode {

    static class TreeLinkNode {
        int val;
        TreeLinkNode left, right, next;
        TreeLinkNode(int x) { val = x; }
        
        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            TreeLinkNode temp = this;
            sb.append("val="+val);
            temp = temp.next;
            while(temp != null) {
                sb.append("->"+temp.val);
                temp = temp.next;
            }
            /*if(temp.left != null)
                sb.append(" left= "+temp.left.val);
            else
                sb.append(" next=null");
            if(temp.next != null)
                sb.append(" next= "+temp.next.val);
            else
                sb.append(" next=null");
            if(temp.right != null)
                sb.append(" right= "+temp.right.val);
            else
                sb.append(" right=null");*/
            return sb.toString();
        }
    }
    
    public static void main(String[] args) {
        TreeLinkNode node = new TreeLinkNode(1);
        node.left = new TreeLinkNode(2);
        node.right = new TreeLinkNode(3);
        node.left.left = new TreeLinkNode(4);
        node.left.right = new TreeLinkNode(5);
//        node.right.left = new TreeLinkNode(6);
        node.right.right = new TreeLinkNode(7);
        connect(node);
        System.out.println("hello");
    }
    
    /**
     * We can solve this problem by Level wise traversal. First thought which comes to mind is level
     * wise traversal. But we can achieve same in O(1) space complexity and O(n) time complexity. We
     * can maintain two pointers, One will be pointing in level i and another in level i+1. When we
     * will reach at level i+1 that time level i mapping we have already done. So we can take
     * advantage of next pointers and avoid using extra memory. But this approach even if I have
     * thought and implemented, It's very much likely that I will forget this in future.
     */
    
    static public void connect(TreeLinkNode root) {
        if (root == null)
            return;
        if (root.left == null && root.right == null) {
            root.next = null;
            return;
        }
        root.next = null;
        TreeLinkNode first = root;
        // Maintain 2 level mapping at same time
        // One for parent level(first) and one for child level(nextLevelStart)
        TreeLinkNode p = first;
        TreeLinkNode c = findFirstInNextLevel(first);
        while (c != null) {
            TreeLinkNode temp = c;
            while (p != null && temp != null) {
                TreeLinkNode[] linkNodes = findNextNode(p, temp);
                if (linkNodes != null) {
                    temp.next = linkNodes[1];
                    p = linkNodes[0];
                    temp = temp.next;
                } else {
                    temp.next = null;
                    p = null;
                }
            }
            p = c;
            c = findFirstInNextLevel(p);
        }
    }
    
    
    /**
     * This Finds node right to temp when parent for temp is p.
     */
    private static TreeLinkNode[] findNextNode(TreeLinkNode p, TreeLinkNode temp) {
        TreeLinkNode[] res = new TreeLinkNode[2];
        if (p.left == temp && p.right != null) {
            res[0] = p;
            res[1] = p.right;
            return res;
        }
        if(temp == p.right || p.right == null) {
            TreeLinkNode p1 = p.next;
            while(p1 != null && p1.left == null && p1.right == null) {
                p1 = p1.next;
            }
            if(p1 != null && p1.left != null) {
                res[0] = p1;
                res[1] = p1.left;
                return res;
            }
            if(p1 != null && p1.right != null) {
                res[0] = p1;
                res[1] = p1.right;
                return res;
            }
        } 
        return null;
    }

    /**
     * This finds node in next level which is not null. Like if first is root and root.left is null
     * but root.right is not null then in that case it will return root.right.
     * 
     * @param first First node in current level.
     * @return first node which is not null from L->R
     */
    private static TreeLinkNode findFirstInNextLevel(TreeLinkNode first) {
        while(first != null) {
            if(first.left != null)
                return first.left;
            if(first.right != null)
                return first.right;
            first = first.next;
        }
        return null;
    }
}
