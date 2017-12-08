package com.learning.geeksforgeeks;
public class RemoveADoubleB {

	public static void main(String[] args) {
		System.out.println(removeADoubleB("acbdbca"));
		System.out.println(removeADoubleB(""));
		System.out.println(removeADoubleB("b"));
		System.out.println(removeADoubleB("aab"));
		System.out.println(removeADoubleB("abb"));
		System.out.println(removeADoubleB("aabb"));
	}

	private static String removeADoubleB(String input) {
		char[] in = input.toCharArray();
		int aCount = 0, bCount = 0;
		for (int i = 0; i < in.length; i++) {
			if (in[i] == 'a')
				aCount++;
			else if (in[i] == 'b')
				bCount++;
		}
		char[] out = new char[in.length + bCount];
		for (int i = 0; i < in.length; i++) {
			out[i] = in[i];
		}
		// Double B
		int index = in.length + bCount - 1;
		for (int i = in.length - 1; i >= 0 && index >= 0; i--) {
			if(out[i] != 'a' && out[i] != 'b') {
				out[index] = out[i];
				index--;
			} else if (out[i] == 'b') {
				out[index] = 'b';
				out[index - 1] = 'b';
				index -= 2;
			} 
		}
//		System.out.println(index);
//		System.out.println(out);
//		System.out.println(new String(out,index+1,in.length+bCount-1-(index+1)+1));
		// remove A
		/*index = 0;
		for(int i=0;i<out.length&&index<out.length-aCount;i++){
			if(out[i] != 'a') {
				out[index] = out[i];
				index++;
			}
		}*/
		/*for (int i = out.length - 1; i >= 0 && index >= 0; i--) {
			if (out[i] != 'a') {
				out[index] = out[i];
				index--;
			}
		}*/
//		System.out.println(new String(out));
		return new String(out,index+1,in.length+bCount-1-(index+1)+1);
	}
}
