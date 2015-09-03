package com.categorize.late;

public class SubsetOfSizeK {

    /**
     * @param args
     */
    public static void main(String [] args) {
        int [] A = {1,2,3};
        findSubsets(A, 2);
    }
     
    private static void findSubsets(int []A, int setSize) {
        int len = A.length;
        int n = (int)Math.pow(2, len);
         
        if (setSize > len) {
            return ;
        }
        for (int i=0; i<n; i++) {
            int set = i;
            int count = 0;
            String str = "";
            System.out.println();
            System.out.println("Running loop for set="+set);
            while (set != 0) {
                System.out.println("set="+set);
                if ((set & 1) != 0) {
                    str += A[count];
                }
                set = set >> 1;
                ++count;
            }
//            if (str.length() == setSize) {
                System.out.println(str);
//            }
        }
    }
}
