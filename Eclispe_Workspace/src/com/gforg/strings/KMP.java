
package com.gforg.strings;

public class KMP {

    static int[] lps;

    public static void main(String[] args) {
        String input = "AAAABABA";
        KMPSearch(input.toCharArray(), "ABA".toCharArray());
    }

    static void KMPSearch(char[] text, char[] pat) {
        computeLPSArray(pat);
        /*for (int i = 0; i < lps.length; i++)
            System.out.print(lps[i] + " ");
        System.out.println();*/
        int j = 0;
        int i = 0;
        while (i < text.length) {
            if (text[i] == pat[j]) {
                j++;
                i++;
            }
            if (j == pat.length) {
                System.out.println("Pattern match at=" + (i - pat.length));
                j = lps[j - 1];
            } else if (i < text.length && text[i] != pat[j]) {
                if (j == 0)
                    i++;
                else
                    j = lps[j - 1];
            }
        }
    }

    static void computeLPSArray(char[] pat) {
        lps = new int[pat.length];
        int M = pat.length;
        int len = 0; // lenght of the previous longest prefix suffix
        int i;

        lps[0] = 0; // lps[0] is always 0
        i = 1;

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pat[i] == pat[len]) {
                len++;
                lps[i] = len;
                i++;
            } else // (pat[i] != pat[len])
            {
                if (len != 0) {
                    // This is tricky. Consider the example AAACAAAA and i = 7.
                    len = lps[len - 1];

                    // Also, note that we do not increment i here
                } else // if (len == 0)
                {
                    lps[i] = 0;
                    i++;
                }
            }
        }
    }
}
