package com.hackerrank.misc.problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * <a href=
 * "https://www.hackerrank.com/challenges/hackerland-radio-transmitters">
 * Problem link</a><br>
 * 
 * <pre>
 * Idea is to always find the farthest house where transmitter can be put starting 
 * from begining. Once transmitter has been put on a house find out it's range and 
 * then start from next house in same way. In this way it is ensured that minimum 
 * number of tranmitters will be used.
 * </pre>
 */
public class Gridland_Metro {

	static Map<Long, List<TrainTrack>> tracks = new HashMap<>(1000);

	static class TrainTrack implements Comparable<TrainTrack> {

		long row;
		long col1;
		long col2;

		public TrainTrack(long row, long col1, long col2) {
			this.row = row;
			this.col1 = col1;
			this.col2 = col2;
		}

		@Override
		public String toString() {
			return "[row=" + row + ",col1=" + col1 + ",col2=" + col2 + "]";
		}

		@Override
		public int compareTo(TrainTrack o) {
			if (this.col1 == o.col1)
				return (int) (this.col2 - o.col2);
			return (int) (this.col1 - o.col1);
		}
	}

	static long gridlandMetro(long n, long m, long k, long[][] track) {
		for (int i = 0; i < k; i++) {
			long r = track[i][0];
			long c1 = track[i][1];
			long c2 = track[i][2];
			List<TrainTrack> trainTracks = tracks.get(r);
			if (null == trainTracks) {
				trainTracks = new LinkedList<>();
			}
			trainTracks.add(new TrainTrack(r, c1, c2));
			tracks.put(r * 1l, trainTracks);
		}
		long count = n * m;
		long blocked = 0;
		// System.out.println(tracks);
		for (Long row : tracks.keySet()) {
			List<TrainTrack> trainTracks = tracks.get(row);
			Collections.sort(trainTracks);
			trainTracks = mergeOverlappingTracks(trainTracks);
			// System.out.println(trainTracks);
			blocked += findOutBlocksForRow(trainTracks);
		}
		// System.out.println("c:" + count);
		// System.out.println("blocked:" + blocked);
		count = count - blocked;
		return count;
	}

	private static List<TrainTrack> mergeOverlappingTracks(List<TrainTrack> trainTracks) {
		int size = trainTracks.size();
		if (size == 1) {
			return trainTracks;
		}
		List<TrainTrack> tracksList = new ArrayList<>(trainTracks.size());
		for (int i = 0; i < size;) {
			TrainTrack start = trainTracks.get(i);
			long c2 = start.col2;
			i++;
			while (i < size && c2 >= trainTracks.get(i).col1) {
				c2 = Math.max(c2, trainTracks.get(i).col2);
				i++;
			}
			tracksList.add(new TrainTrack(start.row, start.col1, c2));
		}
		return tracksList;
	}

	private static long findOutBlocksForRow(List<TrainTrack> trainTracks) {
		int size = trainTracks.size();
		if (size == 1) {
			TrainTrack trainTrack = trainTracks.get(0);
			return trainTrack.col2 - trainTrack.col1 + 1;
		}
		long diff = 0;
		for (int i = 0; i < size; i++) {
			TrainTrack start = trainTracks.get(i);
			long c1 = start.col1;
			long c2 = start.col2;
			diff = diff + c2 - c1 + 1;
		}
		// System.out.println("Diff=" + diff);
		return diff;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		long n = in.nextInt();
		long m = in.nextInt();
		long k = in.nextInt();
		long[][] track = new long[(int) k][3];
		for (int track_i = 0; track_i < k; track_i++) {
			for (int track_j = 0; track_j < 3; track_j++) {
				track[track_i][track_j] = in.nextInt();
			}
		}
		long result = gridlandMetro(n, m, k, track);
		System.out.println(result);
		in.close();
	}

}
