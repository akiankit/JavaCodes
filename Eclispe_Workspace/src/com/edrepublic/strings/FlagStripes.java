package com.edrepublic.strings;

public class FlagStripes {

    /**
     * @param args
     */
    
    static int[][] values;
    public static void main(String[] args) {
        String flag = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
        String flag1 = removeSameCharacters(flag);
        values = new int[flag1.length() + 1][flag1.length() + 1];
//        System.out.println(flag1);
        int num = minNumberOfStrokes(flag1, 0, flag1.length()-1);
        System.out.println(flag + ": " + num);
    }
    
    private static int minNumberOfStrokes(String flag, int start, int end) {
        if (start > end)
            return 0;
        if (start == end) {
            return 1;
        }
        if(values[start][end] != 0)
            return values[start][end];
        int ans = 0;
        if (flag.charAt(start) == flag.charAt(end)) {
            ans= 1 + minNumberOfStrokes(flag, start + 1, end - 1);
        } else {
            ans = 1 + Math.min(minNumberOfStrokes(flag, start + 1, end),
                    minNumberOfStrokes(flag, start, end - 1));
        }
        values[start][end] = ans;
        return values[start][end];
    }

    private static String removeSameCharacters(String flag) {
        char[] input = flag.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length;) {
            char temp = input[i];
            sb.append(input[i]);
            while (i < input.length && temp == input[i]) {
                i++;
            }
        }
        return sb.toString();
    }
}
