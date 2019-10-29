/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.concurrency.samples.chapter5.phaseradvanced.benchmark;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter5.phaseradvanced.common.DataLoader;
import com.concurrency.samples.chapter5.phaseradvanced.common.Individual;
import com.concurrency.samples.chapter5.phaseradvanced.concurrent.ConcurrentGeneticAlgorithm;
import com.concurrency.samples.chapter5.phaseradvanced.serial.SerialGeneticAlgorithm;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class MyBenchmark {

	@Param({ "10", "100", "1000" })
	private int generations;

	@Param({ "100", "1000", "10000" })
	private int individuals;

	@Param({ "lau15_dist", "kn57_dist" })
	private String name;

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, batchSize = 1)
	@Measurement(iterations = 10, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Timeout(time = 1, timeUnit = TimeUnit.DAYS)
	public void serialGeneticAlgorithm() {
		try {

			int[][] distanceMatrix;
			distanceMatrix = DataLoader.load(Paths.get("chapter5_data", name + ".txt"));

			SerialGeneticAlgorithm serialGeneticAlgorithm = new SerialGeneticAlgorithm(distanceMatrix, generations,
					individuals);
			Individual result = serialGeneticAlgorithm.calculate();
			System.out.println("=======================================");
			System.out.println("Example:" + name);
			System.out.println("Generations: " + generations);
			System.out.println("Population: " + individuals);
			System.out.println("Best Individual: " + result);
			System.out.println("Total Distance: " + result.getValue());
			System.out.println("=======================================");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, batchSize = 1)
	@Measurement(iterations = 10, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Timeout(time = 1, timeUnit = TimeUnit.DAYS)
	public void concurrentGeneticAlgorithm() {
		try {

			int[][] distanceMatrix = DataLoader.load(Paths.get("chapter5_data", name + ".txt"));

			ConcurrentGeneticAlgorithm concurrentGeneticAlgorithm = new ConcurrentGeneticAlgorithm(distanceMatrix,
					generations, individuals);
			Individual result = concurrentGeneticAlgorithm.calculate();
			System.out.println("=======================================");
			System.out.println("Example:" + name);
			System.out.println("Generations: " + generations);
			System.out.println("Population: " + individuals);
			System.out.println("Best Individual: " + result);
			System.out.println("Total Distance: " + result.getValue());
			System.out.println("=======================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
