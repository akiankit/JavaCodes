
package com.gforg.misc;

import java.util.HashMap;
import java.util.Map;

import com.initial.util.NumberUtil;

class NumberWithNFactors {

	public static void main(String[] args) {
		long nanoTime = System.currentTimeMillis();
		Map<Long, Long> map = new HashMap<>();
		for (int i = 2; i <= 100; i++) {
			map.put(i * 1l, null);
		}
		// StringBuilder sb = new StringBuilder();
		for (int i = 2; i <= 1000000; i += 2) {
			int list = NumberUtil.totalNumberOfFactors(i);
			if (map.get(list * 1l) == null) {
				map.put(list * 1l, i * 1l);
			}
		}
		System.out.println(System.currentTimeMillis() - nanoTime);
		System.out.println(map);
	}

}
