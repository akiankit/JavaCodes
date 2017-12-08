package com.samsung.training;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Hanaro {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        for (int l = 1; l <= tests; l++) {
            int count = scanner.nextInt();
            int[] x = new int[count];
            int[] y = new int[count];
            GraphEdge[] edges = new GraphEdge[(count * (count - 1)) / 2];
            for (int i = 0; i < count; i++) {
                x[i] = scanner.nextInt();
            }
            for (int i = 0; i < count; i++) {
                y[i] = scanner.nextInt();
            }
            double rate = scanner.nextDouble();
            Point[] points = new Point[count];
            for (int i = 0; i < count; i++) {
                points[i] = new Point(x[i], y[i]);
            }
            int k = 0;
            for (int i = 0; i < count; i++) {
                for (int j = i + 1; j < count; j++) {
                    edges[k] = new GraphEdge(points[i], points[j]);
                    k++;
                }
            }
            KruskalMST kruskal = new KruskalMST();
            List<GraphEdge> mst = kruskal.getMST(points, edges);
            double cost = 0;
            for (GraphEdge graphEdge : mst) {
                cost = cost + graphEdge.distance;
            }
            System.out.println(cost);
            System.out.println("#" + l + " " + Math.round(cost * rate));    
        }
        scanner.close();
    }

}

class DisjointSet {

    private Map<Point, Set<Point>> disjointSet;

    private Map<Point, Point> parents;

    private Map<Point, Integer> rank;

    public DisjointSet() {
        disjointSet = new HashMap<Point, Set<Point>>();
        parents = new HashMap<Point, Point>();
        rank = new HashMap<Point, Integer>();
    }

    public void makeSet(Point point) {
        Set<Point> set = new HashSet<Point>();
        set.add(point);
        parents.put(point, point);
        rank.put(point, 0);
        disjointSet.put(point, set);
    }

    public Point find(Point x) {
        if (parents.get(x) == x)
            return x;
        return find(parents.get(x));
    }

    public void union(Point x, Point y) {
        Point xRep = find(x);
        Point yRep = find(y);
        if (xRep == yRep)
            return;
        if (rank.get(xRep) < rank.get(yRep)) {
            Set<Point> ySet = disjointSet.get(yRep);
            Set<Point> xSet = disjointSet.get(xRep);
            for (Point point : xSet) {
                ySet.add(point);
            }
            parents.put(xRep, yRep);
            disjointSet.put(yRep, ySet);
            disjointSet.remove(xRep);
        } else if (rank.get(xRep) > rank.get(yRep)) {
            Set<Point> xSet = disjointSet.get(xRep);
            Set<Point> ySet = disjointSet.get(yRep);
            for (Point point : ySet) {
                xSet.add(point);
            }
            parents.put(yRep, xRep);
            disjointSet.put(xRep, xSet);
            disjointSet.remove(yRep);
        } else {
            Set<Point> xSet = disjointSet.get(xRep);
            Set<Point> ySet = disjointSet.get(yRep);
            for (Point integer : ySet) {
                xSet.add(integer);
            }
            disjointSet.put(xRep, xSet);
            parents.put(yRep, xRep);
            rank.put(xRep, rank.get(xRep) + 1);
            disjointSet.remove(yRep);
        }
    }

    @Override
    public String toString() {
        return parents.toString() + " " + disjointSet.toString();
    }

}

class KruskalMST {

    public List<GraphEdge> getMST(Point[] vertices, GraphEdge[] edges) {
        List<GraphEdge> list = new LinkedList<GraphEdge>();
        DisjointSet disjointSet = new DisjointSet();
        for (int i = 0; i < vertices.length; i++) {
            disjointSet.makeSet(vertices[i]);
        }
        // System.out.println(Arrays.toString(edges));
         Arrays.sort(edges);
        //System.out.println(Arrays.toString(edges));
        for (int i = 0; i < edges.length; i++) {
            Point u = edges[i].src;
            Point v = edges[i].dest;
            if (disjointSet.find(u) != disjointSet.find(v)) {
                disjointSet.union(u, v);
                list.add(edges[i]);
            }
        }
        return list;
    }
}

class Point {
    int x;

    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

class GraphEdge implements  Comparable<GraphEdge>{
    Point src;
    Point dest;
    double distance;
    
    public GraphEdge(Point src, Point dest) {
        this.src = src;
        this.dest = dest;
        distance = Math.pow(src.x-dest.x,2) + Math.pow(src.y - dest.y, 2);
    }
    
    public void print() {
        System.out.println(this.src + "->" + this.dest + "(" + this.distance + ")");
    }
    
    @Override
    public String toString() {
        return src + "->" + dest;
    }
    
    @Override
    public int compareTo(GraphEdge o) {
        if(this.distance > o.distance)
            return 1;
        else if(this.distance == o.distance)
            return 0;
        else return -1;
    }
}
