package com.structures.ds;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
/*Verified with below inputs
 *
Wikipedia page sample inputs
5 7
A E 1
B A 3
B C 5
C D 2
E B 4
E C 6
E D 7

7 11
A B 7
A D 5
B C 8
B D 9
B E 7
C E 5
D E 15
D F 6
E F 8
E G 9
F G 11

geeks for geeks
9 14
A B 4
A H 8
B H 11
B C 8
C D 7
C I 2
C F 4
D F 14
D E 9
E F 10
F G 2
G I 6
G H 1
H I 7
 * */
public class Graph {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<Integer, Character> codeToName = new HashMap<Integer, Character>();
        Map<Character, Integer> nameToCode = new HashMap<Character, Integer>();
        Scanner scanner = new Scanner(System.in);
        int verticesCount = scanner.nextInt();
        int edgesCount = scanner.nextInt();
        char[] vertices = new char[verticesCount];
        GraphEdge[] edges = new GraphEdge[edgesCount];
        int[] vertexCode = new int[verticesCount];
        
        for (int i = 0; i < verticesCount; i++) {
            vertices[i] = (char)('A' + i);
            vertexCode[i] = i;
            codeToName.put(i, vertices[i]);
            nameToCode.put(vertices[i], i);
        }
        
        for (int i = 0; i < edgesCount; i++) {
            char src = scanner.next().charAt(0);
            char dest = scanner.next().charAt(0);
            int weight = scanner.nextInt();
            edges[i] = new GraphEdge(src, dest, weight);
        }
        
        int[][] adjacenyMatrix = createAdjacencyMatrix(edges,nameToCode, vertices);
        KruskalMST kruskal =  new KruskalMST();
        List<GraphEdge> mst = kruskal.getMST(vertices, edges);
        System.out.println(mst);
        int cost = 0;
        for (GraphEdge graphEdge : mst) {
            cost = cost + graphEdge.weight;
        }
        System.out.println(cost);
        System.out.println("using arrays");
        KruskalMSTUsingArray kruskal2 =  new KruskalMSTUsingArray();
        List<GraphEdge> mst2 = kruskal2.getMST(vertices, edges);
        System.out.println(mst2);
        int cost2 = 0;
        for (GraphEdge graphEdge : mst2) {
            cost2 = cost2 + graphEdge.weight;
        }
        System.out.println(cost2);
//        System.out.println(nameToCode);
//        for (int i = 0; i < adjacenyMatrix.length; i++) {
//            System.out.println(Arrays.toString(adjacenyMatrix[i]));
//        }
        /*Dijkstra shortestPath = new Dijkstra();
        int[][] shortestPathSimpleQueue = shortestPath.getShortestPathSimpleQueue(vertexCode,
                nameToCode.get('A'), adjacenyMatrix);
        for (int i = 0; i < shortestPathSimpleQueue[0].length; i++) {
            System.out.print(codeToName.get(shortestPathSimpleQueue[1][i]) + " ");
        }
        System.out.println(Arrays.toString(shortestPathSimpleQueue[0]));*/
        
        PrimMST prim = new PrimMST();
        int[][] primMST = prim.getPrimMST(vertexCode, adjacenyMatrix);
        for (int i = 0; i < primMST[0].length; i++) {
//            System.out.print(codeToName.get(primMST[1][i]) + " ");
            System.out.print(codeToName.get(primMST[1][i]) + "->"+codeToName.get(i)+" ");
        }
        System.out.println(Arrays.toString(primMST[0]));
        scanner.close();
    }

    private static int[][] createAdjacencyMatrix(GraphEdge[] edges,
            Map<Character, Integer> nameToCode, char[] vertices) {
        int[][] matrix = new int[vertices.length][vertices.length];
        for (int i = 0; i < edges.length; i++) {
            int src = nameToCode.get(edges[i].src);
            int dest = nameToCode.get(edges[i].dest);
            int weight = edges[i].weight;
            matrix[src][dest] = weight;
            // This line can be uncommented and graph will be undirected then Prim can be verified.
            // matrix[dest][src] = weight;
        }
        return matrix;
    }
}

// Prim is only for undirected graph. For directed graph it might not give correct output.
class PrimMST {

    public int[][] getPrimMST(int[] vertices, int[][] adjacenyMatrix) {
        int[][] res = new int[2][vertices.length];
        
        // this is to store distance from starting point
        int[] dist = new int[vertices.length];
        
        // This is to store parent from which we can reach to point i;
        int[] prev = new int[vertices.length];
        
        // initalize a queue and store all vertices.
        Queue<Integer> queue = new ArrayDeque<Integer>();
        for (int i = 0; i < vertices.length; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            queue.add(i);
        }
        
        // Making first point as start of spanning tree.
        dist[0] = 0;
        prev[0] = 0;
        boolean[] mstSet = new boolean[vertices.length];
        while (queue.isEmpty() == false) {
            // Again this can be improved like Dijkstra
            int u = getAndRemoveMinValue(queue, dist);
            mstSet[u] = true;
            for (int v = 0; v < adjacenyMatrix[u].length; v++) {
                // Check for all nodes reachable from u and update distance if possible.
                if (adjacenyMatrix[u][v] != 0 && mstSet[v] == false) {
                    int distance = adjacenyMatrix[u][v];
                    if (distance < dist[v]) {
                        dist[v] = distance;
                        prev[v] = u;
                    }
                }
            }
        }
        res[0] = dist;
        res[1] = prev;
        return res;
    }

    // This method retrieves min value present in queue and removes the value.
    // Min value is fetched according to values present in dist array.
    // Queue actually stores only index of nodes.
    private int getAndRemoveMinValue(Queue<Integer> queue, int[] dist) {
        int min = Integer.MAX_VALUE;
        int res = 0;
        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext()) {
            int index = iterator.next();
            if (min > dist[index]) {
                min = dist[index];
                res = index;
            }
        }
        queue.remove(res);
        return res;
    }

}

class Dijkstra {
    // Commnets are same as of PrimMST
    public int[][] getShortestPathSimpleQueue(int[] vertices, int source, int[][] adjacenyMatrix) {
        int[][] res = new int[2][vertices.length];
        int[] dist = new int[vertices.length];
        int[] prev = new int[vertices.length];
        dist[source] = 0;
        prev[source] = source;
        Queue<Integer> queue = new ArrayDeque<Integer>();
        for (int i = 0; i < vertices.length; i++) {
            if (i != source) {
                dist[i] = Integer.MAX_VALUE;
                prev[i] = -1;
            }
            queue.add(i);
        }
        while (queue.isEmpty() == false) {
            // This part we can modify using priority queue.
            int u = getAndRemoveMinValue(queue, dist);
//            System.out.println("Node with mininum distance="+u);
            for (int v = 0; v < adjacenyMatrix[u].length; v++) {
                if (adjacenyMatrix[u][v] != 0) {
                    int distance = dist[u] + adjacenyMatrix[u][v];
                    if (distance < dist[v]) {
                        dist[v] = distance;
                        prev[v] = u;
                    }
                }
            }
        }
        res[0] = dist;
        res[1] = prev;
//        System.out.println("Distance="+Arrays.toString(dist));
//        System.out.println("Prev="+Arrays.toString(prev));
        return res;
    }

    private int getAndRemoveMinValue(Queue<Integer> queue,int[] dist) {
        int min = Integer.MAX_VALUE;
        int res = 0;
        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext()) {
            int index = iterator.next();
            if (min > dist[index]) {
                min = dist[index];
                res = index;
            }
        }
        queue.remove(res);
        return res;
    }
}

class KruskalMST {
    
    public List<GraphEdge> getMST(char[] vertices, GraphEdge[] edges) {
        List<GraphEdge> list = new LinkedList<GraphEdge>();
        DisjointSet disjointSet = new DisjointSet();
        for(int i=0;i<vertices.length;i++){
            disjointSet.makeSet(vertices[i]);
        }
        Arrays.sort(edges);
//        System.out.println(Arrays.toString(edges));
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i].src;
            int v = edges[i].dest;
            if (disjointSet.find(u) != disjointSet.find(v)) {
                disjointSet.union(u, v);
                list.add(edges[i]);
            }
        }
        return list;
    }
}

class DisjointSetUsingArray {

    private int[] parents;

    private int[] rank;

    public DisjointSetUsingArray(int size) {
        parents = new int[size + 1];
        rank = new int[size + 1];
    }

    public void makeSet(int x) {
        rank[x] = 0;
        parents[x] = x;
    }

    public int find(int x) {
        if (parents[x] == x)
            return x;
        return find(parents[x]);
    }

    public void union(int x, int y) {
        int xRep = find(x);
        int yRep = find(y);
        if (xRep == yRep)
            return;
        if (rank[xRep] < rank[yRep]) {
            parents[xRep] = yRep;
        } else if (rank[yRep] < rank[xRep]) {
            parents[yRep] = xRep;
        }else {
            parents[yRep] = xRep;
            rank[xRep]++;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(parents) + " " + Arrays.toString(rank);
    }

}

class KruskalMSTUsingArray {
    
    public List<GraphEdge> getMST(char[] vertices, GraphEdge[] edges) {

        List<GraphEdge> list = new LinkedList<GraphEdge>();
        DisjointSetUsingArray disjointSet = new DisjointSetUsingArray(vertices.length);
        for (int i = 0; i < vertices.length; i++) {
            disjointSet.makeSet(i);
        }
        Arrays.sort(edges);
        // System.out.println(Arrays.toString(edges));
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i].src - 'A';
            int v = edges[i].dest - 'A';
            if (disjointSet.find(u) != disjointSet.find(v)) {
                disjointSet.union(u, v);
                list.add(edges[i]);
            }
        }
        return list;
    }
}

class GraphEdge implements  Comparable<GraphEdge>{
    char src;
    char dest;
    int weight;
    
    public GraphEdge(char src, char dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
    
    public void print() {
        System.out.println(this.src + "->" + this.dest + "(" + this.weight + ")");
    }
    
    @Override
    public String toString() {
        return src + "->" + dest;
    }
    
    @Override
    public int compareTo(GraphEdge o) {
        return this.weight - o.weight;
    }
}

class Element{
    int value;
    int priority;
    
    Element(int value){
        this.value = value;
        this.priority = value;
    }
    
    Element(int value, int priority) {
        this.value = value;
        this.priority = priority;
    }
}


class DisjointSet {

    private Map<Integer, Set<Integer>> disjointSet;

    private Map<Integer, Integer> parents;

    private Map<Integer, Integer> rank;

    public DisjointSet() {
        disjointSet = new HashMap<Integer, Set<Integer>>();
        parents = new HashMap<Integer, Integer>();
        rank = new HashMap<Integer, Integer>();
    }

    public void makeSet(int x) {
        Set<Integer> set = new HashSet<Integer>();
        set.add(x);
        parents.put(x, x);
        rank.put(x, 0);
        disjointSet.put(x, set);
    }

    public int find(int x) {
        if (parents.get(x) == x)
            return x;
        return find(parents.get(x));
    }

    public void union(int x, int y) {
        int xRep = find(x);
        int yRep = find(y);
        if (xRep == yRep)
            return;
        if (rank.get(xRep) < rank.get(yRep)) {
            Set<Integer> ySet = disjointSet.get(yRep);
            Set<Integer> xSet = disjointSet.get(xRep);
            for (Integer integer : xSet) {
                ySet.add(integer);
            }
            parents.put(xRep, yRep);
            disjointSet.put(yRep, ySet);
            disjointSet.remove(xRep);
        } else if (rank.get(xRep) > rank.get(yRep)) {
            Set<Integer> xSet = disjointSet.get(xRep);
            Set<Integer> ySet = disjointSet.get(yRep);
            for (Integer integer : ySet) {
                xSet.add(integer);
            }
            parents.put(yRep, xRep);
            disjointSet.put(xRep, xSet);
            disjointSet.remove(yRep);
        } else {
            Set<Integer> xSet = disjointSet.get(xRep);
            Set<Integer> ySet = disjointSet.get(yRep);
            for (Integer integer : ySet) {
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
