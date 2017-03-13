/**
 * File Name: ExceptionCreation.java
 * 
 * Copyright (c) 2017 BISON Schweiz AG, All Rights Reserved.
 */

package tech.bison.benchmark;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;

public class ExceptionFakeCreation {

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void measureThroughput(Blackhole bh) {
		bh.consume(new FakeThrowable("some error"));
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void measureAvgTime(Blackhole bh) {
		bh.consume(new FakeThrowable("some error"));
	}

	static class FakeThrowable {
		FakeThrowable(String message) {
		}
	}
}
