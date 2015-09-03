
package com.codechef.practice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class TWTCLOSE {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        String[] words = line.split(" ");
        int n = Integer.parseInt(words[0]);
        boolean[] tweets = new boolean[n];
        int k = Integer.parseInt(words[1]);
        int openTweets = 0;
        for (int i = 0; i < k; i++) {
            line = br.readLine();
            if (line.equals("CLOSEALL")) {
                openTweets = 0;
                tweets = new boolean[n];
            } else {
                words = line.split(" ");
                int tweetNumber = Integer.parseInt(words[1]);
                if (tweets[tweetNumber - 1]) {
                    tweets[tweetNumber - 1] = false;
                    openTweets--;
                } else {
                    tweets[tweetNumber - 1] = true;
                    openTweets++;
                }
            }
            System.out.println(openTweets);
        }
    }

}
