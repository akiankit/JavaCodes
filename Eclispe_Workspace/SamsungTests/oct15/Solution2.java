package com.test.oct15;

import java.util.Scanner;

public class Solution2 {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int testCount = 1; testCount <=10; testCount++) {
			int numberOfPeople = scanner.nextInt();
			int bombPostion = scanner.nextInt();
			int laddersCount = scanner.nextInt();
			int ladderStartRow[] = new int[laddersCount];
			int ladderStartCol[] = new int[laddersCount];
			int ladderEndRow[] = new int[laddersCount];
			int ladderEndCol[] = new int[laddersCount];

			for (int i = 0; i < laddersCount; i++) {
				ladderStartRow[i] = scanner.nextInt();
				ladderStartCol[i] = scanner.nextInt();
				ladderEndRow[i] = scanner.nextInt();
				ladderEndCol[i] = scanner.nextInt();
			}

			int bombCol = bombPostion;
			for (int bombRow = numberOfPeople; bombRow > 0; bombRow--) {
				int startOfLadderIndex = isStartOfLadder(bombRow, bombCol, ladderStartRow, ladderStartCol);
				if (startOfLadderIndex != -1) {
					bombCol = ladderEndCol[startOfLadderIndex];
				}else{
					int endOfLadderIndex = isEndOfLadder(bombRow, bombCol, ladderEndRow, ladderEndCol);
					if (endOfLadderIndex != -1 ) {
						bombCol = ladderStartCol[endOfLadderIndex];
					}
				}
				
			}
			System.out.println("#"+testCount+" "+ bombCol);
		}
		scanner.close();
	}

	private static int isEndOfLadder(int bombRow, int bombCol, int[] ladderEndRow, int[] ladderEndCol) {
		for(int i=0;i<ladderEndRow.length;i++){
			if(ladderEndRow[i] == bombRow && ladderEndCol[i] == bombCol){
				return i;
			}
		}
		return -1;
	}

	private static int isStartOfLadder(int bombRow, int bombCol, int[] ladderStartRow, int[] ladderStartCol) {
		for(int i=0;i<ladderStartCol.length;i++){
			if(ladderStartRow[i] == bombRow && ladderStartCol[i] == bombCol){
				return i;
			}
		}
		return -1;
	}
}
