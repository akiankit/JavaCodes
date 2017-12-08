package com.gforg.strings;

public class RunLengthEncoding {

    public static void main(String[] args) {
        System.out.println(encode("awwwww"));
    }
    
    private static String encode(String input) {
        char[] in = input.toCharArray();
        int outLength=in.length*2;
        char[] out = new char[outLength];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i];
        }
        int j = out.length -1;
        for (int i = in.length - 1; i >= 0 && j >= 0; i--) {
            int count = 1;
            while (i > 0 && out[i] == out[i - 1]) {
                count++;
                i--;
            }
            while (count != 0) {
                out[j] = (char)((char)(count % 10) + '0');
                j--;
                count = count / 10;
            }
            out[j] = out[i];
            j--;
        }
        return new String(out,j+1,out.length-j-1);
    }
}
