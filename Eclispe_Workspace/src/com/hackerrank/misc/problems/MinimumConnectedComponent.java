package com.hackerrank.misc.problems;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class MinimumConnectedComponent {
	
	static class InputReader {

		private InputStream stream;

		private byte[] buf = new byte[1024];

		private int curChar;

		private int numChars;

		private SpaceCharFilter filter;

		public InputReader(InputStream stream) {
			this.stream = stream;
		}

		public int read() {
			if (numChars == -1)
				throw new InputMismatchException();
			if (curChar >= numChars) {
				curChar = 0;
				try {
					numChars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (numChars <= 0)
					return -1;
			}
			return buf[curChar++];
		}

		public int readInt() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			int res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}

		public String next() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		public long readLong() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			long res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}

		public boolean isSpaceChar(int c) {
			if (filter != null)
				return filter.isSpaceChar(c);
			return isWhitespace(c);
		}

		public static boolean isWhitespace(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		public interface SpaceCharFilter {
			public boolean isSpaceChar(int ch);
		}
	}

	static class OutputWriter {
		private final PrintWriter writer;

		public OutputWriter(OutputStream outputStream) {
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					outputStream)));
		}

		public void printLine(String string) {
			writer.println(string);
		}

		public void close() {
			writer.close();
		}

		public void printLine(long i) {
			writer.println(i);
		}
	}

	public static void main(String[] args) {
		InputReader ir = new InputReader(System.in);
		int n = ir.readInt();
		int q = ir.readInt();
		int[] weights = new int[n];
		for (int i = 0; i < n; i++)
			weights[i] = ir.readInt();
		Kruskal kruskal = new Kruskal(n, weights);
		for (int i = 1; i <= n; i++) {
			kruskal.makeSet(i);
		}
//		kruskal.toString();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < q; i++) {
			int a = ir.readInt();
			int b = ir.readInt();
			int res = kruskal.union(a, b);
			sb.append(res).append("\n");
//			kruskal.toString();
		}
		System.out.println(sb);
	}
}

class SetWeight implements Comparable<SetWeight>{
	int leader;
	int weight;
	
	public SetWeight(int x, int i) {
		this.leader = x;
		this.weight = i;
	}
	
	@Override
	public String toString() {
		return leader + "," + weight;
	}

	@Override
	public int compareTo(SetWeight o) {
		if(this.weight == o.weight)
			return 0;
		return this.weight > o.weight ? -1 : 1;   
	}
	
	@Override
	public int hashCode() {
		return this.leader;
	}
	
	@Override
	public boolean equals(Object obj) {
		SetWeight temp = (SetWeight) obj;
		if(weight != temp.weight || leader != temp.leader)
			return false;
		return true;
	}
}

class WeightComparator implements Comparator<SetWeight>{
	
	@Override
	public int compare(SetWeight o1, SetWeight o2) {
		return o1.weight - o2.weight;
	}
}

class Kruskal {

	private Map<Integer, SetWeight> map = new HashMap<Integer,SetWeight>();
	private TreeSet<SetWeight> set;
	
    private int[] parents;
    
    private int[] weights;

    private int[] rank;
    
    private int[] size;
    
    public int getSize(int x) {
        return size[find(x)];
    }

    public Kruskal(int count,int[] weights) {
        parents = new int[count + 1];
        rank = new int[count + 1];
        size = new int[count + 1];
        this.weights = new int[count+1];
        this.weights[0] = Integer.MAX_VALUE;
		for (int i = 1; i <= count; i++) {
			this.weights[i] = weights[i - 1];
		}
		set = new TreeSet<SetWeight>();
    }

    public void makeSet(int x) {
        rank[x] = 0;
        parents[x] = x;
        size[x] = 1;
        SetWeight set1 = new SetWeight(x,weights[x]);
        map.put(x, set1);
        this.set.add(set1);
        System.out.println(this);
    }

    public int find(int x) {
        if (parents[x] == x)
            return x;
        return find(parents[x]);
    }

	public int union(int x, int y) {
		int xRep = find(x);
		int yRep = find(y);
		if (xRep == yRep)
			return set.first().weight;
		SetWeight xWeight = map.get(xRep);
		SetWeight yWeight = map.get(yRep);
		map.remove(xRep);
		map.remove(yRep);
		set.remove(xWeight);
		set.remove(yWeight);
//		queue.remove(xWeight);
//		queue.remove(yWeight);
		if (rank[xRep] < rank[yRep]) {
			parents[xRep] = yRep;
			size[yRep] += size[xRep];
			weights[yRep] += weights[xRep];
			SetWeight weight = new SetWeight(yRep, weights[yRep]);
			map.put(yRep, weight);
			set.add(weight);
		} else {
			parents[yRep] = xRep;
			rank[xRep]++;
			size[xRep] += size[yRep];
			weights[xRep] += weights[yRep];
			SetWeight weight = new SetWeight(xRep, weights[xRep]);
			map.put(xRep, weight);
			set.add(weight);
//			queue.offer(weight);
		}
		return set.first().weight;
	}
    
    @Override
    public String toString() {
    	System.out.println(set.toString());
        return "";
    }

}

