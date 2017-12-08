package com.gforg.strings;

public class RemoveADoubleB {

    public static void main(String[] args) {
        System.out.println(removeADoubleBAshok("acbdbca"));
        System.out.println(removeADoubleBAshok("a"));
        System.out.println(removeADoubleBAshok("b"));
        System.out.println(removeADoubleBAshok("aab"));
        System.out.println(removeADoubleBAshok("abb"));
        System.out.println(removeADoubleBAshok("aabb"));
    }

    private static String removeADoubleBAshok(String input) {
        char[] in = input.toCharArray();
        int aCount = 0, bCount = 0;
        for (int i = 0; i < in.length; i++) {
            if (in[i] == 'a')
                aCount++;
            else if (in[i] == 'b')
                bCount++;
        }
        
        char[] out = new char[in.length + bCount - aCount];
        
        for(int i = 0, j = 0; i < in.length && j < out.length;) {
            if(in[i] == 'b') {
                out[j] = 'b';
                j++;
                out[j] = 'b';
                j++;
                i++;
            } else if(in[i] != 'a') {
                out[j] = in[i];
                j++;
                i++;
            } else {
                i++;
            }
        }
        
        return String.valueOf(out);
    }
    private static String removeADoubleBInPlace(String input) {
        char[] in = input.toCharArray();
        int aCount = 0, bCount = 0;
        for (int i = 0; i < in.length; i++) {
            if (in[i] == 'a')
                aCount++;
            else if (in[i] == 'b')
                bCount++;
        }
        char[] out = new char[in.length + bCount];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i];
        }
        // Double B
        int index = in.length + bCount - 1;
        for (int i = in.length - 1; i >= 0 && index >= 0; i--) {
            if (out[i] == 'b') {
                out[index] = 'b';
                out[index - 1] = 'b';
                index -= 2;
            } else {
                out[index] = out[i];
                index--;
            }
        }
        // remove A
        index = 0;
        for (int i = 0; i < out.length && index < out.length - aCount; i++) {
            if (out[i] != 'a') {
                out[index] = out[i];
                index++;
            }
        }
        // System.out.println(new String(out));
        return new String(out, 0, out.length - aCount);
    }
    
    public static String removeADoubleB(String input) {
        int aCount = 0, bCount = 0;
        int inputLength = input.length();
        for(int i=0;i<inputLength;i++) {
            if(input.charAt(i) == 'a') {
                aCount++;
            } else if(input.charAt(i) == 'b') {
                bCount++;
            }
        }
        int outputLength = inputLength - aCount + bCount;
        char[] in = input.toCharArray();
        char[] out = new char[outputLength];
        for(int i=0,k=0;i<in.length;i++){
            if(in[i] == 'b'){
                out[k++]='b';
                out[k++]='b';
            }else if(in[i]!='a'){
                out[k++]=in[i];
            }
        } 

        return new String(out);
    }
}