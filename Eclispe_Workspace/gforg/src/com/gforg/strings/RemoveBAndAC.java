package com.gforg.strings;

public class RemoveBAndAC {

    public static void main(String[] args) {
        System.out.println(removeBAndAC("aabcc"));
    }

    // http://www.geeksforgeeks.org/remove-a-and-bc-from-a-given-string/
    private static String removeBAndAC(String input) {
        char[] in = input.toCharArray();
        int state = 1, j = 0;
        for (int i = 0; i < in.length; i++) {
            if (state == 1 && in[i] != 'a' && in[i] != 'b') {
                in[j] = in[i];
                j++;
            }
            if (state == 2 && in[i] != 'c') {
                in[j] = 'a';
                j++;
                if (in[i] != 'a' && in[i] != 'b') {
                    in[j] = in[i];
                    j++;
                }
            }
            // This condition is to ensure that all  'ac' is removed from input string.
            if (j > 1 && in[j - 2] == 'a' && in[j - 1] == 'c')
                j = j - 2;
            state = in[i] == 'a' ? 2 : 1;
        }
        return new String(in, 0, j);
    }
    
    private static String removeAC(String input) {
        char[] in = input.toCharArray();
        int count = 0;
        for (int i = 0, k = 0; i < in.length;) {
            if (in[i] != 'a' && in[i] != 'b') {
                k = Math.max(k, 0);
                in[k] = in[i];
                k++;
                i++;
            } else if(in[i] == 'b') {
                while(i<in.length && in[i] == 'b') {
                    count++;
                    i++;
                }
                k = k-1;
                while (k >= 0 && in[k] == 'a' && in[i] == 'c' && i <= in.length) {
                    k--;
                    i++;
                    count+=2;
                }
            } else {
                k = Math.max(k, 0);
                if (i != in.length - 1 && in[i + 1] == 'c') {
                    i = i + 2;
                    if (i <= in.length - 1) {
                        in[k] = in[i];
                        k++;
                        i++;
                        count += 2;
                    } else if (i == in.length)
                        count += 2;
                } else {
                    in[k] = in[i];
                    k++;
                    i++;
                }
            }
        }
        return new String(in,0,in.length-count);
    }
    
}
