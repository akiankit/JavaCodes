package com.samsung.test;

public class Vanishing_Color_Blocks {

	public static final int SIZE = 10;
	public static final int NO_ITR = 10;

	public static int[][][] map = new int[NO_ITR][SIZE][SIZE];

	public static int run_test(int[][] box) {
		int clicks = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int number = box[i][j];
				if(number==0){
					continue;
				}else{
					vanishNumber(number,box,i,j);
					clicks++;
					//displayMap(box);
				}
			}
		}
		
		return clicks;
	}

	private static void vanishNumber(int number, int[][] box, int i, int j) {
		box[i][j]=0;
		if(j!=SIZE-1 && box[i][j+1] ==number){
			vanishNumber(number, box, i, j+1);
		}
		if(i!=SIZE-1 && box[i+1][j]==number){
			vanishNumber(number, box, i+1, j);
		}
		if(i!=SIZE-1 && j!=SIZE-1 && box[i+1][j+1]==number){
			vanishNumber(number, box, i+1, j+1);
		}
		if(i!=SIZE-1 && j!=0 && box[i+1][j-1]==number){
			vanishNumber(number, box, i+1, j-1);
		}
		if(i!=0 && j != SIZE-1 && box[i-1][j+1] == number){
			vanishNumber(number, box, i-1, j+1);
		}
		return;
	}

	public static void build_map_clr() {
		int i, j, k, w;

		for (k = 0; k < NO_ITR; k++) {
			for (i = 0; i < SIZE; i++) {
				for (j = 0; j < SIZE; j++) {
					w = (int)(Math.random()*4 + 1);
					map[k][i][j] = w;
				}
			}
		}

	}
	
	public static void main(String[] args) {
		int count;
		build_map_clr();
		/*displayMap(map[0]);
		run_test(map[0]);
		displayMap(map[0]);*/
		for (count = 0; count < NO_ITR; count++) {
			displayMap(map[count]);
			System.out.println(run_test(map[count]));
		}
		return;
	}

	private static void displayMap(int[][] map2) {
		System.out.println("---------------start-----------");
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.print(map2[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("---------------end-----------");
	}

}
