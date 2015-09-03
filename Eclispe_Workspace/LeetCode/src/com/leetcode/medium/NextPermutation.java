package com.leetcode.medium;

import java.util.Arrays;

public class NextPermutation {

    public static void main(String[] args) {
        int[] temp = {1,2,5,4,3};
//        System.out.println(Arrays.toString(temp));
        NextPermutation permutation = new NextPermutation();
//        permutation.nextPermutation(temp);
//        System.out.println(Arrays.toString(temp));
        String input = "ab";
        char[] array = input.toCharArray();
        
    }

    
    private void nextPermutation(int[] nums) {
        int num1Index = findCorruptedElement(nums);
        int num1=0;
        if(num1Index != -1) {
            num1 = nums[num1Index];
        }else{
            num1Index = 0;
            Arrays.sort(nums);
            return;
        }
        int num2Index = findNextGreater(nums, num1);
        if(num2Index == -1) {
            return;
        }
        swap(nums, num1Index,num2Index);
        reverseArray(nums,num1Index+1);
    }
    
    private String nextPermutationString(char[] nums) {
        int num1Index = findCorruptedElement(nums);
        int num1 = 0;
        if (num1Index != -1) {
            num1 = nums[num1Index];
        } else {
            return "no answer";
        }
        int num2Index = findNextGreater(nums, num1);
        if (num2Index == -1) {
            return "no answer";
        }
        swap(nums, num1Index, num2Index);
        reverseArray(nums, num1Index + 1);
        return String.valueOf(nums);
    }
    
    private void reverseArray(int[] nums, int start) {
        for (int i = start, j = nums.length - 1; i <= j; i++, j--) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }
    
    private void reverseArray(char[] nums, int start) {
        for (int i = start, j = nums.length - 1; i <= j; i++, j--) {
            char temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }


    private void swap(int[] permutation, int num1Index, int num2Index) {
        int temp = permutation[num1Index];
        permutation[num1Index] = permutation[num2Index];
        permutation[num2Index] = temp;
    }
    
    private void swap(char[] permutation, int num1Index, int num2Index) {
        char temp = permutation[num1Index];
        permutation[num1Index] = permutation[num2Index];
        permutation[num2Index] = temp;
    }

    private int findNextGreater(int[] permutation, int num1) {
        for(int i=permutation.length-1;i>0;i--) {
            if(permutation[i] > num1) {
                return i;
            }
        }
        return -1;
    }
    
    private int findNextGreater(char[] permutation, int num1) {
        for(int i=permutation.length-1;i>0;i--) {
            if(permutation[i] > num1) {
                return i;
            }
        }
        return -1;
    }

    private int findCorruptedElement(int[] permutation) {
        for (int i = permutation.length - 1; i > 0; i--) {
            if (permutation[i - 1] < permutation[i]) {
                return i - 1;
            }
        }
        return -1;
    }
    
    private int findCorruptedElement(char[] permutation) {
        for (int i = permutation.length - 1; i > 0; i--) {
            if (permutation[i - 1] < permutation[i]) {
                return i - 1;
            }
        }
        return -1;
    }

    
    @SuppressWarnings("unused")
    private void countSort(int[] arr, int start, int end) {
        int[] count = new int[10];
        for (int i = start; i <= end; i++) {
            count[arr[i]]++;
        }
        int k = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] != 0) {
                arr[start + k] = i;
                k++;
                count[i]--;
                i--;
            }
        }
    }

}
