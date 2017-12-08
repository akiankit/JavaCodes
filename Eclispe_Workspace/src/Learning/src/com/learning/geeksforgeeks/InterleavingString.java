package com.learning.geeksforgeeks;

public class InterleavingString {

	//http://www.geeksforgeeks.org/check-whether-a-given-string-is-an-interleaving-of-two-other-given-strings-set-2/
	public static void main(String[] args) {
		System.out.println(isInterleave("XXY", "XXZ", "XXZXXXY"));
		System.out.println(isInterleave("XY", "WZ", "WZXY"));
		System.out.println(isInterleave("XY", "X", "XXY"));
		System.out.println(isInterleave("YX", "X", "XXY"));
		System.out.println(isInterleave("XXY", "XXZ", "XXXXZY"));
		System.out.println(isInterleave("aabcc", "dbbca", "aadbbcbcac"));
		System.out.println(isInterleave("aabcc", "dbbca", "aadbbbaccc"));
	}

	public static boolean isInterleave(String s1, String s2, String s3) {
		int M = s1.length();
		int N = s2.length();
		if(s3.length() !=  M +N)
			return false;
		int i = 0, j = 0;
		boolean[][] interLeave = new boolean[M + 1][N + 1];
		for (i = 0; i <= M; i++) {
			for (j = 0; j <= N; j++) {
				if (i == 0 && j == 0)// If both strings are empty that means
										// IL
					interLeave[i][j] = true;
				else if (i == 0 && s2.charAt(j - 1) == s3.charAt(j - 1))
					interLeave[i][j] = interLeave[i][j - 1];
				else if (j == 0 && s1.charAt(i - 1) == s3.charAt(i - 1))
					interLeave[i][j] = interLeave[i - 1][j];
				else if (i != 0 && j != 0
						&& s3.charAt(i + j - 1) == s1.charAt(i - 1)
						&& s3.charAt(i + j - 1) != s2.charAt(j - 1))
					interLeave[i][j] = interLeave[i - 1][j];
				else if (i != 0 && j != 0
						&& s3.charAt(i + j - 1) == s2.charAt(j - 1)
						&& s3.charAt(i + j - 1) != s1.charAt(i - 1))
					interLeave[i][j] = interLeave[i][j - 1];
				else if (i != 0 && j != 0
						&& s3.charAt(i + j - 1) == s2.charAt(j - 1)
						&& s3.charAt(i + j - 1) == s1.charAt(i - 1))
					interLeave[i][j] = interLeave[i - 1][j]
							|| interLeave[i][j - 1];
			}
		}
		return interLeave[M][N];
	}

	// This approach will work when s1 and s2 are having different chars
	public static boolean isInterleaveForDifferentChars(String s1, String s2, String s3) {
		int i1 = 0;
		int i2 = 0;
		if (s3.length() != (s1.length() + s2.length()))
			return false;
		for (int i = 0; i < s3.length(); i++) {
			char c = s3.charAt(i);
			if (i1 < s1.length() && s1.charAt(i1) == c) {
				i1++;
			} else if (i2 < s2.length() && s2.charAt(i2) == c) {
				i2++;
			} else {
				return false;
			}
		}
		return true;
	}
}
