package com.codechef.practice;

import java.util.Scanner;

public class JOHNY {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		for (int i = 0; i < testCases; i++) {
			int numberOfSongs = scanner.nextInt();
			int songsLeghts[] = new int[numberOfSongs];
			for(int j=0;j<numberOfSongs;j++){
				songsLeghts[j] = scanner.nextInt();
			}
			int uncleJohnyPosition = scanner.nextInt();
			int uncleJohnySongLength = songsLeghts[uncleJohnyPosition-1];
			int answer = 0;
			for(int j=0;j<numberOfSongs;j++){
				if(songsLeghts[j] < uncleJohnySongLength){
					answer++;
				}
			}
			System.out.println(answer+1);
			/*songsLeghts = quickSort(songsLeghts, 0,numberOfSongs-1);
			int answer = binarySearch(songsLeghts, 0, numberOfSongs-1, uncleJohnySongLength);*/
			//System.out.println(answer+1);
		}
		scanner.close();
	}
	
	/*public static int[] quickSort(int[] input, int startIndex, int lastIndex) {
		if (startIndex < lastIndex) {
			int q = partition(input, startIndex, lastIndex);
			quickSort(input, startIndex, q);
			quickSort(input, q + 1, lastIndex);
		}
		return input;
	}

	private static int partition(int[] input, int startIndex, int lastIndex) {
		//System.out.println("partition called with startIndex=" + startIndex + " lastIndex=" + lastIndex);
		int pivot = input[lastIndex];
		int i = startIndex;
		int j = lastIndex ;
		while (true) {
			while (input[j] > pivot) {
				j--;
			}
			while ( i< lastIndex &&  input[i] < pivot) {
				i++;
			}
			if (i <= j) {
				//System.out.println("Swapping " + input[i] + " and " + input[j]);
				int temp = input[i];
				input[i] = input[j];
				input[j] = temp;
				i++;j--;
			} else {
				//System.out.println("returning value of j="+j);
				return j;
			}
		}
	}
	
	private static int binarySearch(int[] paramArrayOfLong, int start, int end, long numberToSearch) {
		int i = start;
		int j = end - 1;
		while (i <= j) {
			int k = i + j >>> 1;
			long l = paramArrayOfLong[k];
			if (l < numberToSearch)
				i = k + 1;
			else if (l > numberToSearch)
				j = k - 1;
			else
				return k;
		}
		return (-(i + 1));
	}*/

}
