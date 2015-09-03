
package com.codechef.practice;

import java.util.Scanner;

class FLIPCOIN {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int coinsCount = scanner.nextInt();
        int[] coins = new int[coinsCount];
        SegmentTree segmentTree = new SegmentTree();
        STNode tree = segmentTree.constructSegmentTree(coins, 0, coinsCount - 1);
        int queries = scanner.nextInt();
        for (int i = 0; i < queries; i++) {
            int queryType = scanner.nextInt();
            if (queryType == 1) {
                int sum = segmentTree.getSum(tree, scanner.nextInt(), scanner.nextInt());
                System.out.println(sum);
            } else {
                int startIndex = scanner.nextInt();
                int endIndex = scanner.nextInt();
                for(int j= startIndex;j<=endIndex;j++){
                    segmentTree.updateValueAtIndex(tree, j);
                }
            }
        }
        scanner.close();
    }

}

class STNode {
    int leftIndex;

    int rightIndex;

    int sum;

    STNode leftNode;

    STNode rightNode;
}

class SegmentTree {

    STNode constructSegmentTree(int[] A, int l, int r) {
        if (l == r) {
            STNode node = new STNode();
            node.leftIndex = l;
            node.rightIndex = r;
            node.sum = A[l];
            return node;
        }
        int mid = (l + r) / 2;
        STNode leftNode = constructSegmentTree(A, l, mid);
        STNode rightNode = constructSegmentTree(A, mid+1, r);
        STNode root = new STNode();
        root.leftIndex = leftNode.leftIndex;
        root.rightIndex = rightNode.rightIndex;
        root.sum = leftNode.sum + rightNode.sum;
        root.leftNode = leftNode;
        root.rightNode = rightNode;
        return root;
    }

    int getSum(STNode root, int l, int r) {
        if (root.leftIndex >= l && root.rightIndex <= r) {
            return root.sum;
        }
        if (root.rightIndex < l || root.leftIndex > r) {
            return 0;
        }
        return getSum(root.leftNode, l, r) + getSum(root.rightNode, l, r);
    }

    /**
     * @param root
     * @param index index of number to be updated in original array
     * @param newValue
     * @return difference between new and old values
     */
    int updateValueAtIndex(STNode root, int index) {
        int diff = 0;
        if (root.leftIndex == root.rightIndex && index == root.leftIndex) {
            // We actually reached to the leaf node to be updated
            int newValue = root.sum^1;
            diff = newValue - root.sum;
            root.sum = newValue;
            return diff;
        }
        int mid = (root.leftIndex + root.rightIndex) / 2;
        if (index <= mid) {
            diff = updateValueAtIndex(root.leftNode, index);
        } else {
            diff = updateValueAtIndex(root.rightNode, index);
        }
        root.sum += diff;
        return diff;
    }
}
