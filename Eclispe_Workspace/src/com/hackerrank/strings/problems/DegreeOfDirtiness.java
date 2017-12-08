package com.hackerrank.strings.problems;

import java.util.Scanner;

public class DegreeOfDirtiness {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for(int i=0;i<tests;i++) {
            int N = scanner.nextInt();
            int M = scanner.nextInt();
            int ans = 0;
            int index = 0;
            // If M is less than N then no worry
            if(M<=N){
                ans = 0;
                if((M&1) == 1){
                    // If M is odd then from start
                    index = (M+1)/2;
                }else{
                    // If M is even then from last 
                    index = N-((M-1)/2);
                }
            } else{
                if((N&1) == 0) {
                    ans = ans + M/N;
                    M = M%N;
                    if((M&1) == 1){
                        index = (M+1)/2;
                    }else{
                        index = N-((M-1)/2);
                    }
                } else{
                    ans = ans + M/(2*N);
                    ans = ans*2;
                    M = M%(2*N);
                    if((M&1)==1){
                        if(M>N) {
                            M = M-N;
                            ans = ans +1;
                        }
                        index = (M+1)/2;
                    }else{
                        if(M>N) {
                            M = M-N+1;
                            ans = ans +1;
                        }
                        index = N - M/2 +1;
                    }
                }    
            }
            System.out.println(index+" "+ans);
        }
    }

}
