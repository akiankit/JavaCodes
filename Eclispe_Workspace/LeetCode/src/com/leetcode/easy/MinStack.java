package com.leetcode.easy;

import java.util.Arrays;

public class MinStack {
	
	public static void main(String[] args) {
		MinStack minStack = new MinStack();
		minStack.push(-3);
		System.out.println(minStack.getMin());
	}
    
    int capacity = 100000;

    int[] arr1 = new int[capacity];
    int arr1Index = -1;
    int[] arr2 = new int[capacity];
    int arr2Index = -1;    

    private void resize() {
        int[] temp = Arrays.copyOf(arr1, arr1Index);
        capacity = 2 * capacity;
        arr1 = new int[capacity];
        for (int i = 0; i < arr1Index; i++) {
            arr1[i] = temp[i];
        }
        
        temp = Arrays.copyOf(arr2, arr1Index);
        arr2 = new int[capacity];
        for (int i = 0; i < arr1Index; i++) {
            arr2[i] = temp[i];
        }
    }

    public void push(int x) {
        arr1Index++;
        if(x==capacity)
            resize();
        arr1[arr1Index] = x;
        arr2Index++;
        if(arr2Index ==0){
            arr2[arr2Index] = 0;
        }else{
            int num1 = x;
            int num2 = arr1[arr2[arr2Index-1]];
            if(num1<num2)
                arr2[arr2Index] = arr1Index;
            else
                arr2[arr2Index] = arr2[arr2Index-1];
        }
    }

    public void pop() {
        arr2Index--;
        arr1Index--;
    }

    public int top() {
        return arr1[arr1Index];
    }

    public int getMin() {
        return arr1[arr2[arr2Index]];
    }
}
