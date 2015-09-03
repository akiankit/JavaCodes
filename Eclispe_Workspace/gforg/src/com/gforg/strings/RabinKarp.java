
package com.gforg.strings;

public class RabinKarp {

    public static void main(String[] args) {
        String input = "AABAABA";
        search(input.toCharArray(), "ABA".toCharArray(), 101);
    }

    private static void search(char[] txt, char[] pat, int q) {
        int M = pat.length;
        int N = txt.length;
        int i, j;
        int p = 0; // hash value for pattern
        int t = 0; // hash value for txt
        int h = 1;
        int d = 256;

        // The value of h would be "pow(d, M-1)%q"
        for (i = 0; i < M - 1; i++)
            h = (h * d) % q;

        // Calculate the hash value of pattern and first window of text
        for (i = 0; i < M; i++) {
            p = (d * p + pat[i]) % q;
            t = (d * t + txt[i]) % q;
        }

        // Slide the pattern over text one by one
        for (i = M; i <= N; i++) {

            // Check the hash values of current window of text and pattern
            // If the hash values match then only check for characters on by one
            if (p == t) {
                /* Check for characters one by one */
                for (j = 0; j < M; j++) {
                    if (txt[i - M + j] != pat[j])
                        break;
                }
                if (j == M) // if p == t and pat[0...M-1] = txt[i, i+1, ...i+M-1]
                {
                    System.out.println("Pattern found at index=" + i);
                }
            }

            // Calculate hash value for next window of text: Remove leading digit,
            // add trailing digit
            if (i < N) {
                t = (d * (t - txt[i - M] * h) + txt[i]) % q;

                // We might get negative value of t, converting it to positive
                if (t < 0)
                    t = (t + q);
            }
        }
    }

}
