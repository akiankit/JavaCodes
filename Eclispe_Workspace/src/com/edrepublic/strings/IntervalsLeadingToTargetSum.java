package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IntervalsLeadingToTargetSum {
   
    static List<Range> list = new LinkedList<Range>();
    static Map<Range,Boolean> visited = new HashMap<Range,Boolean>();
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        long num = Long.parseLong(str);
        long length = (num + 1) / 2;
        System.out.println(length);
        long tempSum = 1;
        for (long i = 1; i < length; i++) {
            tempSum += (i + 1);
            if (tempSum == num) {
                list.add(new Range(1, i + 1));
            }
        }
        findPairsWithDifference(new Range(0, length - 1), num);
        Collections.sort(list);
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != list.size() - 1) {
                System.out.print(",");
            }
        }
    }
    
    private static void findPairsWithDifference(Range range, long num) {
        long start = range.start;
        long end = range.end;
        if (start > end)
            return;
        if (visited.containsKey(range))
            return;
        visited.put(range, true);
        double startSum = (start + 1) * (start + 2) / 2;
        double endSum = (end + 1) * (end + 2) / 2;
        if (num == endSum - startSum) {
            list.add(new Range(start + 2, end + 1));
        } else if (num < endSum - startSum) {
            findPairsWithDifference(new Range(start + 1, end), num);
            findPairsWithDifference(new Range(start, end - 1), num);
        }
    }
    
}

class Range implements Comparable<Range>{
    long start;
    long end;
    
    Range(long m, long l){
        this.start = m;
        this.end = l;
    }
    
    @Override
    public String toString() {
        return "["+start+","+end+"]";
    }
    
    @Override
    public boolean equals(Object obj) {
        Range temp = (Range)obj;
        return temp.start == this.start && temp.end == this.end;
    }

    @Override
    public int compareTo(Range o) {
        return (int)(this.start - o.start);
    }
    
    @Override
    public int hashCode() {
        return (int)start;
    }
}
