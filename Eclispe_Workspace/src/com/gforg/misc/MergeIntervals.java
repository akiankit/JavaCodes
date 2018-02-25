
package com.gforg.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MergeIntervals {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		int t = Integer.parseInt(line);
		for (int i = 0; i < t; i++) {
			line = sc.nextLine();
			int num = Integer.parseInt(line);
			line = sc.nextLine();
			String[] words = line.split(" ");
			Interval[] intervals = new Interval[num];
			int index = 0;
			for (int j = 0; j < words.length; j += 2) {
				int d1 = Integer.parseInt(words[j]);
				int d2 = Integer.parseInt(words[j + 1]);
				intervals[index++] = new Interval(d1, d2);
			}
			mergeIntervals(intervals);
		}
		sc.close();
	}

	public static void mergeIntervals(Interval[] intervals) {
		// System.out.println(Arrays.toString(intervals));
		Arrays.sort(intervals);
		List<Interval> list = mergeOverlappingTracks(intervals);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).col1 + " " + list.get(i).col2 + " ");
		}

	}

	private static List<Interval> mergeOverlappingTracks(Interval[] intervals) {
		int size = intervals.length;
		List<Interval> newIntervals = new ArrayList<>(size);
		if (size == 1) {
			newIntervals.add(intervals[0]);
			return newIntervals;
		}
		for (int i = 0; i < size;) {
			Interval start = intervals[i];
			int c2 = start.col2;
			i++;
			while (i < size && c2 >= intervals[i].col1) {
				c2 = Math.max(c2, intervals[i].col2);
				i++;
			}
			newIntervals.add(new Interval(start.col1, c2));
		}
		return newIntervals;
	}

	static class Interval implements Comparable<Interval> {

		int col1;
		int col2;

		public Interval(int col1, int col2) {
			this.col1 = col1;
			this.col2 = col2;
		}

		@Override
		public String toString() {
			return "[col1=" + col1 + ",col2=" + col2 + "]";
		}

		@Override
		public int compareTo(Interval o) {
			if (this.col1 == o.col1)
				return this.col2 - o.col2;
			return this.col1 - o.col1;
		}
	}

}
