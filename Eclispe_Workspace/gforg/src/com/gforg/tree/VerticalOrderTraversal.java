package com.gforg.tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.structures.ds.BSTNode;

public class VerticalOrderTraversal {

    /**
     * @param args
     */
//    1
//    /    \
//   2      3
//  / \    / \
// 4   5  6   7
//         \   \
//          8   9 
    public static void main(String[] args) {
        BSTNode root = new BSTNode(1);
        root.left = new BSTNode(2);
        root.right = new BSTNode(3);
        root.left.right = new BSTNode(4);
        root.left.right.right = new BSTNode(5);
        root.left.right.right.right = new BSTNode(6);
//        root.left.left = new BSTNode(4);
//        root.left.right = new BSTNode(5);
//        root.right.left = new BSTNode(6);
//        root.right.right = new BSTNode(7);
//        root.right.left.right = new BSTNode(8);
//        root.right.left.right.right = new BSTNode(9);
        int[] minMax = new int[2];
        getMinMaxDistance(root, 0, minMax);
        System.out.println("=======Set1======");
        System.out.println(Arrays.toString(minMax));
        verticalOrderTraversalSet1(root, minMax);
        System.out.println("=======Set2======");
        Map<Integer, LinkedList<Integer>> map = new HashMap<Integer, LinkedList<Integer>>();
        verticalOrderTraversalSet2(root, minMax,map);
    }

    //http://www.geeksforgeeks.org/print-binary-tree-vertical-order-set-2/
    private static void verticalOrderTraversalSet2(BSTNode root, int[] minMax,
            Map<Integer, LinkedList<Integer>> map) {
        verticalOrderTraversal2(root, 0, map, minMax);
        System.out.println(minMax[0] +" "+minMax[1] );
        for(int i=minMax[0];i<=minMax[1];i++) {
            System.out.println(map.get(i));
        }
    }

    public static void verticalOrderTraversal2(BSTNode root, int distance,
            Map<Integer, LinkedList<Integer>> map, int[] minMax) {
        if (root == null)
            return;
        else if (distance < minMax[0])
            minMax[0] = distance;
        else if (distance > minMax[1]) {
            minMax[1] = distance;
        }
        LinkedList<Integer> list = map.get(distance);
        if (list == null) {
            list = new LinkedList<Integer>();
        }
        list.add(root.val);
        map.put(distance, list);
        verticalOrderTraversal2(root.left, distance - 1, map, minMax);
        verticalOrderTraversal2(root.right, distance + 1, map, minMax);
    }

    // http://www.geeksforgeeks.org/print-binary-tree-vertical-order/
    // Horizontal distance of a node is distance from root. Left is -1 and right is +1
    public static void verticalOrderTraversalSet1(BSTNode root,int[] minMax) {
        for(int i=minMax[0];i<=minMax[1];i++) {
            System.out.print("Distance="+i+" Nodes= ");
            printNodeAtDistance(root,i,0);
            System.out.println();
        }
    }

    private static void printNodeAtDistance(BSTNode root, int distance, int current) {
        if (root == null)
            return;
        if (current == distance) {
            System.out.print(root.val+" ");
        }
        printNodeAtDistance(root.left, distance, current - 1);
        printNodeAtDistance(root.right, distance, current + 1);
    }

    public static void getMinMaxDistance(BSTNode root, int distance, int[] minMax) {
        if(root == null)
            return;
        else if(distance < minMax[0]) 
            minMax[0] = distance;
        else if (distance > minMax[1]) {
            minMax[1] = distance;
        }
        getMinMaxDistance(root.left, distance-1, minMax);
        getMinMaxDistance(root.right, distance+1, minMax);
    }
    
}
