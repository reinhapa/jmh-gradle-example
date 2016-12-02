/**
 * File Name: AttributeNameToPosition.java
 * 
 * Copyright (c) 2016 BISON Schweiz AG, All Rights Reserved.
 */

package tech.bison.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import gnu.trove.TObjectIntHashMap;

@State(Scope.Thread)
public class AttributeNameToPosition_Trove {
	List<String> keys = new ArrayList<>(1000);
	TObjectIntHashMap<String> map = new TObjectIntHashMap<>();

	@Setup
	public void prepare() {
		for (int i = 0; i < 1000; i++) {
			String key = Integer.toString(i);
			keys.add(key);
			map.put(key, i);
		}
		Collections.shuffle(keys);
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void measureThroughput(Blackhole bh) {
		for (String key : keys) {
			bh.consume(map.get(key));
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void measureAvgTime(Blackhole bh) {
		for (String key : keys) {
			bh.consume(map.get(key));
		}
	}

}
