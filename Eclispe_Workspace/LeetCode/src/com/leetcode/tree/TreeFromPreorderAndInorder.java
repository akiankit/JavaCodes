package com.leetcode.tree;

public class TreeFromPreorderAndInorder {
	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
		
		@Override
		public String toString() {
			return "val="+val;
		}
		
		public static void inorder(TreeNode root) {
			if(root == null)
				return;
			inorder(root.left);
			System.out.print(root.val+",");
			inorder(root.right);
		}
	}
	
	public static void main(String[] args) {
		int[] io = {1,3,2,4};
		int[] po = {1,2,3,4};
		TreeNode root = buildTree(po, io);
		TreeNode.inorder(root);
	}

	public static TreeNode buildTree(int[] preorder, int[] inorder) {
		if (preorder == null || preorder.length == 0)
			return null;
		return generateTree(inorder, 0, inorder.length - 1, preorder, 0,
				preorder.length - 1);
	}

	public static TreeNode generateTree(int[] inorder, int is, int ie,
			int[] preorder, int ps, int pe) {
		if (is > ie)
			return null;
		if (is == ie)
			return new TreeNode(inorder[is]);
		TreeNode root = new TreeNode(preorder[ps]);
		int rootIndexIo = indexOfRoot(inorder, preorder[ps], is, ie);
		int leftLength = rootIndexIo - is + 1-1;
//		int rightLength = ie - rootIndexIo + 1+1;
//		System.out.println("Inorder left=("+is+"-"+(is + leftLength - 1)+")");
//		System.out.println("Preorder left=("+(ps+1)+"-"+(ps+1 + leftLength - 1)+")");
//		System.out.println("Inorder right=("+(rootIndexIo+1)+"-"+(ie)+")");
//		System.out.println("Preorder right=("+(ps+ 1 + leftLength)+"-"+(pe)+")");
		root.left = generateTree(inorder, is, is + leftLength - 1, preorder,
				ps + 1, ps + 1 + leftLength - 1);
		root.right = generateTree(inorder, rootIndexIo + 1, ie, preorder, ps
				+ 1 + leftLength, pe);
		return root;
	}

	private static int indexOfRoot(int[] inorder, int rootValue, int is, int ie) {
		for (int i = is; i <= ie; i++)
			if (inorder[i] == rootValue)
				return i;
		return -1;
	}
}
