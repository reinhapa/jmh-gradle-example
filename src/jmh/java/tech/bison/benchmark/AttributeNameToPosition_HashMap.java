/**
 * File Name: AttributeNameToPosition.java
 * 
 * Copyright (c) 2016 BISON Schweiz AG, All Rights Reserved.
 */

package tech.bison.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class AttributeNameToPosition_HashMap {
	@Param({"1", "20", "40", "60","80", "100", "200", "300", "350", "400", "500", "600", "700"})
	int size;
	List<String> keys;
	HashMap<String, Integer> map;

	@Setup
	public void prepare() {
		keys = new ArrayList<>(size);
		map = new HashMap<>();
		for (int i = 0; i < size; i++) {
			String key = Integer.toString(i);
			keys.add(key);
			map.put(key, Integer.valueOf(i));
		}
		Collections.shuffle(keys);
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void measureThroughput(Blackhole bh) {
		for (String key : keys) {
			bh.consume(getValue(map.get(key)));
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void measureAvgTime(Blackhole bh) {
		for (String key : keys) {
			bh.consume(getValue(map.get(key)));
		}
	}

	private static int getValue(Integer value) {
		if (value == null) {
			return -1;
		}
		return value.intValue();
	}
}
