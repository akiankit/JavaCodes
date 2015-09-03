package com.codechef.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MARCHA2 {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {

			int T = Integer.parseInt(reader.readLine());
			for (int t = 0; t < T; t++) {
				boolean valid = true;
				int stems = 1;
				int k = Integer.parseInt(reader.readLine());
				String[] line = reader.readLine().split(" ");
				for (int i = 1; i <= k; i++) {
					int curr = Integer.parseInt(line[i - 1]);
					if (i == k) {
						valid = stems == curr;
						break;
					} else if (curr > stems) {
						valid = false;
						break;
					}
					stems = stems * 2 - curr * 2;
				}
				if (valid) {
					System.out.println("Yes");
				} else {
					System.out.println("No");
				}
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}