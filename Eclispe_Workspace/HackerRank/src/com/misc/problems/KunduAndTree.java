
package com.misc.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class KunduAndTree {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        Edge[] blackEdge = new Edge[N];
        Edge[] redEdge = new Edge[N];
        int i = 0, k = 0;
        for (int j = 0; j < N - 1; j++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            String type = scanner.next();
            if (type.equals("b")) {
                blackEdge[i] = new Edge(a, b, 0);
                i++;
            } else {
                redEdge[k] = new Edge(a, b, 1);
                k++;
            }
        }
        System.out.println(numberOfPossibleTriplets(N, blackEdge));
        scanner.close();
    }
    
    private static int numberOfPossibleTriplets(int N, Edge[] blackEdges) {
        int mod = 1000000007;
        Set<Triplet> set = new HashSet<Triplet>(); 
        for (Edge edge : blackEdges) {
            if(edge == null)
                break;
            for (int i = 1; i <= N; i++) {
                if (i != edge.start && i != edge.end) {
                    Triplet triplet = new Triplet(edge.start, edge.end, i);
                    set.add(triplet);
                }
            }
        }
        long all = N;
        all = (all * (all - 1)*(all-2))/ 6;
        return (int)((all - set.size()) % mod);
    }
}

class Triplet {
    int a;
    int b;
    int c;
    
    public Triplet(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public boolean equals(Object obj) {
        Triplet triplet = (Triplet)obj;
        if (triplet.a == this.a) {
            if (triplet.b == this.b) {
                return triplet.c == this.c;
            } else if (triplet.b == this.c) {
                return triplet.c == this.b;
            }
        } else if (triplet.b == this.a) {
            if (triplet.a == this.c) {
                return triplet.c == this.b;
            } else if (triplet.c == this.c) {
                return triplet.a == this.b;
            }
        } else if (triplet.c == this.a) {
            if (triplet.a == this.b) {
                return triplet.b == this.c;
            } else if (triplet.b == this.b) {
                return triplet.a == this.c;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.a*29 + this.b*31 + this.c*37;
    }
}

class Edge {
    int start;

    int end;

    int type;// 0 is for black and 1 is for red

    public Edge(int start, int end, int type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }
}

