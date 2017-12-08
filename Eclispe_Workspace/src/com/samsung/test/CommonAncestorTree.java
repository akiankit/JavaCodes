package com.samsung.test;

import java.util.Scanner;

public class CommonAncestorTree {
    
    public static void main(String args[]) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int T;
        T = scanner.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            int n = scanner.nextInt();
            int v = scanner.nextInt();
            int node1 = scanner.nextInt();
            int node2 = scanner.nextInt();
            int[] parents = new int[n+1];
            int[] leftChild = new int[n+1];
            int[] rightChild = new int[n+1];
            for (int i = 0; i < v; i++) {
                int parent = scanner.nextInt();
                int child = scanner.nextInt();
                parents[child] = parent;
                if(leftChild[parent] ==0){
                    leftChild[parent] = child;
                }else{
                    rightChild[parent] = child;
                }
            }
            int[] path1 = new int[n+1];
            int[] path2 = new int[n+1];
            int temp = node1;
            path1[0] = node1;
            path2[0] = node2;
            int k = 1;
            while (parents[temp] != 0) {
                path1[k] = parents[temp];
                temp = parents[temp];
                k++;
            }
            int len1 = k-1;
            temp = node2;
            k = 1;
            while (parents[temp] != 0) {
                path2[k] = parents[temp];
                temp = parents[temp];
                k++;
            }
            int len2 = k-1;
            while(len1 >=0 && len2>=0 && path1[len1] == path2[len2]){
                len1--;
                len2--;
            }
            System.out.println("#" + test_case + " " + path1[len1 + 1] + " "
                    + getSizeOfTree(path1[len1 + 1], parents, leftChild, rightChild));
        }
        scanner.close();
    }
    
    private static int getSizeOfTree(int parent, int[] parents, int[] leftChild, int[] rightChild) {
        if (leftChild[parent] == 0 && rightChild[parent] == 0) {
            return 1;
        } else {
            if (leftChild[parent] != 0 && rightChild[parent] != 0) {
                return 1 + getSizeOfTree(leftChild[parent], parents, leftChild, rightChild)
                        + getSizeOfTree(rightChild[parent], parents, leftChild, rightChild);
            } else if (leftChild[parent] == 0) {
                return 1 + getSizeOfTree(rightChild[parent], parents, leftChild, rightChild);
            } else {
                return 1 + getSizeOfTree(leftChild[parent], parents, leftChild, rightChild);
            }
        }
    }

}
