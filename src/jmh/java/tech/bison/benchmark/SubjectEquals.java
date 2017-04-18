/**
 * File Name: SubjectEquals.java
 * 
 * Copyright (c) 2017 BISON Schweiz AG, All Rights Reserved.
 */

package tech.bison.benchmark;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.security.auth.Subject;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class SubjectEquals {
	@Param({ "1", "10" })
	int size;

	Subject subject1;
	Subject subject2;

	@Setup
	public void setUp() {
		Set<Principal> principals = new HashSet<>();
		Set<Object> pubCredentials = new HashSet<>();
		Set<Object> privCredentials = new HashSet<>();
		for (int i = 0; i < size; i++) {
			principals.add(new SimplePrincipal("user" + i));
			pubCredentials.add("publicCredential" + i);
			privCredentials.add("privateCredential" + i);
		}
		subject1 = new Subject(false, principals, pubCredentials, privCredentials);
		subject2 = new Subject(false, principals, pubCredentials, privCredentials);
	}

	@Benchmark
	@Threads(5)
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void equalsDirect(Blackhole bh) {
		bh.consume(subject1.equals(subject2));
	}

	@Threads(5)
	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void equalsWithStrategy(Blackhole bh) {
		bh.consume(new EqualsStrategy(subject1).isEqualTo(subject2));
	}

	static class EqualsStrategy {
		private final Subject subject;
		private Set<Principal> principals;
		private Set<Object> pubCredentials;
		private Set<Object> privCredentials;

		EqualsStrategy(Subject subject) {
			this.subject = subject;
		}

		boolean isEqualTo(Subject other) {
			if (other == null) {
				return false;
			}
			if (subject == other) {
				return true;
			}
			if (principals == null) {
				Set<Principal> thisPrincipals = subject.getPrincipals();
				synchronized (thisPrincipals) {
					principals = new HashSet<>(thisPrincipals);
				}
			}
			Set<Principal> otherPrincipals = other.getPrincipals();
			if (!(principals.size() == otherPrincipals.size()
					&& otherPrincipals.stream().allMatch(principals::contains))) {
				return false;
			}
			if (pubCredentials == null) {
				Set<Object> thisPublicCredentials = subject.getPublicCredentials();
				synchronized (thisPublicCredentials) {
					pubCredentials = new HashSet<>(thisPublicCredentials);
				}
			}
			Set<Object> otherPublicCredentials = other.getPublicCredentials();
			if (!(pubCredentials.size() == otherPublicCredentials.size()
					&& otherPublicCredentials.stream().allMatch(pubCredentials::contains))) {
				return false;
			}
			if (privCredentials == null) {
				Set<Object> thisPrivateCredentials = subject.getPrivateCredentials();
				synchronized (thisPrivateCredentials) {
					privCredentials = new HashSet<>(thisPrivateCredentials);
				}
			}
			Set<Object> otherPrivateCredentials = other.getPrivateCredentials();
			return privCredentials.size() == otherPrincipals.size()
					&& otherPrivateCredentials.stream().allMatch(privCredentials::contains);
		}
	}

	static class SimplePrincipal implements Principal {
		private String user;

		SimplePrincipal(String user) {
			this.user = user;
		}

		@Override
		public String getName() {
			return user;
		}

		@Override
		public int hashCode() {
			return user.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (!(obj instanceof SimplePrincipal)) {
				return false;
			}
			return user.equals(((SimplePrincipal) obj).user);
		}
	}
}
