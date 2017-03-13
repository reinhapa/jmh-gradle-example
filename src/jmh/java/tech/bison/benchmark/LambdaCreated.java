/**
 * File Name: LambdaCreated.java
 * 
 * Copyright (c) 2017 BISON Schweiz AG, All Rights Reserved.
 */

package tech.bison.benchmark;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class LambdaCreated {
	@Param({ "1" })
	Integer value;

	@Benchmark
	@BenchmarkMode(Mode.All)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void measureAvgTime(Blackhole bh) {
		bh.consume(new TestClass(value));
	}

	static class TestClass {
		Function<Integer, String> function;

		TestClass(Integer param) {
			function = p -> p.toString();
		}
	}

}
