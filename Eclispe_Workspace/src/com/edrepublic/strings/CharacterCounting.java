package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CharacterCounting {

    public static void main(String [] args) throws Exception{ 
        BufferedReader in = new BufferedReader( 
            new InputStreamReader(System.in)); 
        String str = in.readLine(); 
        int n = Integer.parseInt(str);
        int count = 0;
        int letterCount = 0;
        for(int i=0;i<n;i++) {
            str = in.readLine();
            count = 0;
            for(int j=0;j<str.length();j++) {
                if(str.charAt(j) == '~')
                    count++;
            }
            letterCount += str.length() - count;
        }
        System.out.println(letterCount);
    } 

}
