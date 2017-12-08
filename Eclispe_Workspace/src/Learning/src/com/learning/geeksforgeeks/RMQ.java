package com.learning.geeksforgeeks;

import java.util.Arrays;


public class RMQ {
	
	private int preprocess[][] = null;

	public static void main(String[] args) {
		RMQ rmq = new RMQ();
		int[] array = {1,2,4,3,2,6,7,3,9};
//		int[] array = {1,2,3,4,5,6,7,8,9};
		rmq.preprocessArray(array);
		for(int i =0;i<rmq.preprocess.length;i++) {
			System.out.println(Arrays.toString(rmq.preprocess[i]));
		}
//		System.out.println(rmq.getMin(2, 4, array));
		for(int i=0;i<array.length;i++){
			for(int j=i+1;j<array.length;j++) {
				System.out.println("Min b/w("+(i+1)+"-"+(j+1)+")="+rmq.getMin(i, j, array));
			}
		}
	} 
	
	public void preprocessArray(int[] array) {
		int length  = max2PowerLessThanNumber(array.length); 
		int arrayLen = array.length;
		preprocess  = new int[length][];
		for(int i=0;i<length;i++) {
			if (i==0) {
				preprocess[i] = new int[arrayLen];
				for(int j = 0;j<arrayLen;j++) {
					preprocess[i][j] = array[j];
				}
			} else{
				int len =  (int) (arrayLen - (Math.pow(2,i)-1));
				preprocess[i] = new int[len];
				int indexGap = (int) Math.pow(2,i-1);
				for(int j = 0;j<len;j++) {
					preprocess[i][j] = Math.min(preprocess[i-1][j],preprocess[i-1][j+indexGap]);
				}
			}
		}
 	}
	
	public int getMin(int L, int R, int[] array) {
		if(L == R)
			return array[L];
		int length = R-L+1;
		if(isPowerOfTwo(length)) {
			int index = max2PowerLessThanNumber(length);
//			System.out.println(" Row = "+index+" Col="+L);
			return preprocess[index][L];
		}else{
			int index = max2PowerLessThanNumber(length)-1;
			int secondCol = (int) (R-Math.pow(2,index)+1);
//			System.out.println(" Row = "+index+" FirstCol="+L+" SecondCol = "+secondCol);
			return Math.min(preprocess[index][L], preprocess[index][secondCol]);
		}
	}
	
	private boolean isPowerOfTwo(int length) {
		int i = 1;
		while (i < length) {
			i = i << 1;
			if (i == length)
				return true;
		}
		return false;
	}

	public int max2PowerLessThanNumber(int len) {
		if(len <2)
			return len; 
		int t = 1;
		int count = 0;
		while(t < len){
			t = t<<1;
			count++;
		}
		return count;
	}
}
