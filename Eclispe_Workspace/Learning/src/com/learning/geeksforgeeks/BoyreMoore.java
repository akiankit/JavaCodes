package com.learning.geeksforgeeks;
public class BoyreMoore{
	
	public static void main(String[] args) {
		String text = "TRUSTHARDTHOOTHBRUSHES";
		String pattern = "THOOTH";
		text ="roadrunner";
		pattern="runner";
		BoyreMoore boyreMoore = new BoyreMoore();
		boyreMoore.doPatternExists(text, pattern);
	}

	// https://www.youtube.com/watch?v=PHXAOKQk2dw
	/**
	 * Steps: There are 3 steps which I have followed.
	 * 1. Create shift array.
	 * 2. Start matching from right most character.
	 * 3. On mismatch find value of shift for right most character of text with which we started mismatching.
	 * 	  It does not matter at which position mismatch is happening. Always check value for right most
	 *    character in text for which we started matching.
	 * @param text Text in which pattern is to be found.
	 * @param pattern pattern to be found
	 * @return true if pattern is found. If pattern is found then print all indexes value where pattern is found
	 */
	public void doPatternExists(String text, String pattern) {
		int[] shiftArray=null;
		shiftArray = createShiftArray(pattern,shiftArray);
//		System.out.println(Arrays.toString(shiftArray));
		int n = text.length();
		int m = pattern.length();
		int count = 0;
		for(int i=0;i<=n-m;){
			int shift = isMatch(text, i,pattern, shiftArray);
			if (shift == -1) {
				System.out.println("pattern found at:"+(i+1));
				i++;
				count++;
			}
			else
				i= i+ shift;
		}
		if(count==0){
			System.out.println("Sorry!!! pattern does not exist in text");
		}
	}

	/**
	 * 
	 * @param text
	 *            Complete text in which we are looking for pattern.
	 * @param startIdexInText
	 *            Index in text which needs to be checked if match is happening.
	 * @param pattern
	 *            pattern which needs to be found.
	 * @param shiftArray
	 *            Preprocessed shift array.
	 * @return -1 is match is successful otherwise returns the value by which
	 *         pattern should be shifted
	 */
	public int isMatch(String text, int startIdexInText, String pattern, int[] shiftArray) {
		int patternLength = pattern.length();
		int lastIndexInText = startIdexInText+ patternLength-1;
		int lastIndexInPattern = patternLength-1;
		int k = lastIndexInPattern;
		for(int i=lastIndexInText;i>=startIdexInText;i--,k--){
			if(text.charAt(i) != pattern.charAt(k))
				return getShiftValue(text.charAt(lastIndexInText), shiftArray);
		}
		return -1;
	}

	/**
	 * Steps to create shiftArray. 
	 * Value for char present at index i =(length(p) - 1-i).
	 * Ignore last character. For all other characters present
	 * in pattern store value. If some character is getting repeated then
	 * overwrite value with new value.
	 * 
	 * @param pattern
	 *            Pattern which needs to be found.
	 * @param shiftArray
	 *            An array with values as 0 with length equal to alphabet.
	 * @return shiftArray with proper values to shift in case of mismatch.
	 */
	public int[] createShiftArray(String pattern, int[] shiftArray){
		int length = 256;
		int patternLength = pattern.length();
		shiftArray = new int[length];
		for(int i=0;i<length;i++) {
			shiftArray[i] = patternLength;
		}
		for(int i=0;i<patternLength-1;i++){
			shiftArray[pattern.charAt(i)] = patternLength-1-i;
		}
		return shiftArray;
	}

	public int getShiftValue(char mismatchChar, int[] shiftArray) {
		return shiftArray[mismatchChar];
	}
}