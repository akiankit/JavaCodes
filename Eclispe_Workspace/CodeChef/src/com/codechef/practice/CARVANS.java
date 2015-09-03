package com.codechef.practice;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CARVANS {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = br.readLine();
		int testCases = Integer.parseInt(line);
		for (int i = 0; i < testCases; i++) {
			int numOfCars = Integer.parseInt(br.readLine());
			int minSpeed = Integer.MAX_VALUE;
			int maxSpeedSame = 0;
			line = br.readLine();
			String[] speeds = line.split("\\s+");
			for(int j=0;j< numOfCars;j++){
				int speed = Integer.parseInt(speeds[j]);
				if(speed <= minSpeed){
					minSpeed = speed;
					maxSpeedSame++;
				}
			}
			System.out.println(maxSpeedSame);
		}
	}
}
