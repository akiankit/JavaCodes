package com.categorize.late;


//http://en.wikipedia.org/wiki/Longest_common_subsequence_problem

public class LCS {

	public static void main(String[] args) {
		String first = "RBKBGRBGGG";
		String second = "BGKRBRKBGB";
		int firstLength = first.length();
		int secondLength = second.length();
		int[][] lcsLength = getLCSLength(first, second);
		System.out.println(lcsLength[first.length()][second.length()]);
		String subSequence = "";
		subSequence = readLCS(lcsLength,first, second, firstLength, secondLength, subSequence);
		System.out.println(subSequence);
	}

	public static int[][] getLCSLength(String first, String second) {
		int firstLength = first.length();
		int secondLength = second.length();
		int[][] lcsLength = new int[firstLength + 1][secondLength + 1];
		for (int i = 0; i < firstLength + 1; i++) {
			lcsLength[i][0] = 0;
		}
		for (int i = 0; i < secondLength + 1; i++) {
			lcsLength[0][secondLength] = 0;
		}
		for (int i = 1; i < firstLength + 1; i++) {
			for (int j = 1; j < secondLength + 1; j++) {
				if (first.charAt(i - 1) == second.charAt(j - 1)) {
					lcsLength[i][j] = lcsLength[i - 1][j - 1] + 1;
				} else {
					lcsLength[i][j] = Math.max(lcsLength[i - 1][j], lcsLength[i][j - 1]);
				}
			}
		}
		/*for (int i = 0; i < firstLength + 1; i++) {
            for (int j = 0; j < secondLength + 1; j++) {
                System.out.print(lcsLength[i][j]+" ");
            }
            System.out.println();
		}*/
		return lcsLength;
	}

    public static String readLCS(int[][] lcsLength, String first, String second, int firstLength,
            int secondLength, String subSequence) {
        if (firstLength <= 0 || secondLength <= 0) {
            return "";
        } else if (first.charAt(firstLength - 1) == second.charAt(secondLength - 1)) {
            subSequence = first.substring(firstLength - 1, firstLength);
            subSequence = readLCS(lcsLength, first, second, firstLength - 1, secondLength - 1,
                    subSequence) + subSequence;
        } else if (lcsLength[firstLength][secondLength - 1] > lcsLength[firstLength - 1][secondLength]) {
            subSequence = readLCS(lcsLength, first, second, firstLength, secondLength - 1,
                    subSequence);
        } else {
            subSequence = readLCS(lcsLength, first, second, firstLength - 1, secondLength,
                    subSequence);
        }
        return subSequence;
    }
	
}
