package com.gforg.tree;

public class TreeFromPreOrderAndInOrder {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
        
        
        public void printInorder(TreeNode root) {
            if (root == null)
                return;

            printInorder(root.left);

            System.out.print(root.val + ",");

            printInorder(root.right);
        }
    }
        /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] inorder = {4,2,5,1,6,3,7};
        int[] postorder = {4,5,2,6,7,3,1};
        TreeFromPreOrderAndInOrder temp = new TreeFromPreOrderAndInOrder();
        TreeNode root = temp.buildTree(postorder, inorder);
        root.printInorder(root);
    }

    public TreeNode buildTree(int[] postorder, int[] inorder) {
        if (postorder == null || postorder.length == 0)
            return null;
        return generateTree(inorder, 0, inorder.length - 1, postorder, 0,
                postorder.length - 1);
    }
    
    public TreeNode generateTree(int[] inorder, int is, int ie,
            int[] postorder, int ps, int pe) {
        if (is > ie)
            return null;
        if (is == ie)
            return new TreeNode(inorder[is]);
        TreeNode root = new TreeNode(postorder[pe]);
        int rootIndexIo = indexOfRoot(inorder, postorder[pe], is, ie);
        int leftLength = rootIndexIo -1 - is + 1;
//      int rightLength = ie - rootIndexIo + 1+1;
//      System.out.println("Inorder left=("+is+"-"+(is + leftLength - 1)+")");
//      System.out.println("Preorder left=("+(ps+1)+"-"+(ps+1 + leftLength - 1)+")");
//      System.out.println("Inorder right=("+(rootIndexIo+1)+"-"+(ie)+")");
//      System.out.println("Preorder right=("+(ps+ 1 + leftLength)+"-"+(pe)+")");
        root.left = generateTree(inorder, is, is + leftLength - 1, postorder, ps, ps + leftLength - 1);
        root.right = generateTree(inorder, rootIndexIo + 1, ie, postorder, ps + leftLength, pe-1);
        return root;
    }

    private int indexOfRoot(int[] inorder, int rootValue, int is, int ie) {
        for (int i = is; i <= ie; i++)
            if (inorder[i] == rootValue)
                return i;
        return -1;
    }
}
