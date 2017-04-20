/**
 * File Name: AttributeNameToPosition.java
 * 
 * Copyright (c) 2016 BISON Schweiz AG, All Rights Reserved.
 */

package tech.bison.benchmark;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class IdGeneration {

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void measureThroughput_UUID(Blackhole bh) {
		bh.consume(UUID.randomUUID().toString());
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void measureAvgTime_UUID(Blackhole bh) {
		bh.consume(UUID.randomUUID().toString());
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void measureThroughput_BisonUUID(Blackhole bh) {
		bh.consume(BisonUID.fastUUID());
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void measureAvgTime_BisonUUID_extended(Blackhole bh) {
		bh.consume(BisonUID.fastUUID());
	}

	static class BisonUID {
		private static final AtomicLong COUNTER = new AtomicLong();

		public static UUID fastUUID() {
			long mostSigBits = COUNTER.incrementAndGet();
			mostSigBits = mostSigBits << 16;
			// set version to random (4)
			mostSigBits |= 0x4000;
			// set variant to Leach-Salz (2)
			long leastSigBits = 0x1;
			leastSigBits = leastSigBits << 63;
			// add counter
			leastSigBits |= System.nanoTime();
			return new UUID(mostSigBits, leastSigBits);
		}
	}
}
