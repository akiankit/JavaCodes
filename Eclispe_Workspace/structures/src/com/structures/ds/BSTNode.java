package com.structures.ds;

public class BSTNode {
    public BSTNode left;

    public BSTNode right;

    public int val;

    public BSTNode(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this == null)
            return "null";
        if (left != null) {
            sb.append("left=" + left.val);
        } else {
            sb.append("left=null");
        }
        sb.append(",val=" + val);
        if (right != null) {
            sb.append(",right=" + right.val);
        } else {
            sb.append(",right=null");
        }
        return sb.toString();
    }
    
    static public void printInorder(BSTNode root) {
        if (root == null)
            return;

        /* first recur on left child */
        printInorder(root.left);

        /* then print the data of node */
        System.out.print(root.val + ",");

        /* now recur on right child */
        printInorder(root.right);
    }
    
    static public int height(BSTNode root) {
        if (root == null)
            return 0;
        return 1 + Math.max(height(root.left), height(root.right));
    }
    
    public static int diameter1(BSTNode root) {
        return getDiameter(root)[0];
    }
    
    ////http://www.geeksforgeeks.org/diameter-of-a-binary-tree/
    public static int diameter(BSTNode root) {
        if (root == null)
            return 0;
        int leftHeight = BSTNode.height(root.left);
        int rightHeight = BSTNode.height(root.right);
        return Math.max(leftHeight + rightHeight + 1,
                Math.max(diameter(root.left), diameter(root.right)));
    }
    
    //http://www.geeksforgeeks.org/diameter-of-a-binary-tree/
    public static int[] getDiameter(BSTNode root) {
        int[] result = new int[] {
                0, 0
        }; // 1st element: diameter, 2nd: height
        if (root == null)
            return result;
        int[] leftResult = getDiameter(root.left);
        int[] rightResult = getDiameter(root.right);
        int height = Math.max(leftResult[1], rightResult[1]) + 1;
        int rootDiameter = leftResult[1] + rightResult[1] + 1;
        int leftDiameter = leftResult[0];
        int rightDiameter = rightResult[0];
        result[0] = Math.max(rootDiameter, Math.max(leftDiameter, rightDiameter));
        result[1] = height;
        return result;
    }
    
    //http://www.geeksforgeeks.org/double-tree/
    public static void makeDoubleTree(BSTNode root) {
        if(root == null)
            return;
        BSTNode temp = root.left;
        root.left = new BSTNode(root.val);
        root.left.left = temp;
        makeDoubleTree(root.left.left);
        makeDoubleTree(root.right);
    }
}
