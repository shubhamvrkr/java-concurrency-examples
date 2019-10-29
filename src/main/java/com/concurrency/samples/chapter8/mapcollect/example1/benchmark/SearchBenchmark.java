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

package com.concurrency.samples.chapter8.mapcollect.example1.benchmark;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main.ConcurrentObjectAccumulator;
import com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main.ConcurrentStringAccumulator;
import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;
import com.concurrency.samples.chapter8.mapcollect.example1.serial.main.SerialObjectAccumulator;
import com.concurrency.samples.chapter8.mapcollect.example1.serial.main.SerialStringAccumulator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class SearchBenchmark {

	private Path file = Paths.get("chapter8_data/example1");
    
	@Param({"patterns","java","tree"})
	private String query;
	

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentBasicSearch() {
		try {
			List<String> results = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.parallel()
					.filter(f -> f.toString().endsWith(".txt"))
					.collect(ArrayList<String>::new,
							new ConcurrentStringAccumulator(query),
							List::addAll);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentSearch() {
		try {
			List<Product> results = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.parallel()
					.filter(f -> f.toString()
					.endsWith(".txt"))
					.collect(ArrayList<Product>::new,
							new ConcurrentObjectAccumulator(query),
							List::addAll);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialBasicSearch() {
		try {
			ArrayList<String> results=Files.walk(file ,FileVisitOption.FOLLOW_LINKS).filter(f -> f.toString().endsWith(".txt")).collect(
					ArrayList<String>::new,
					new SerialStringAccumulator(query),
					ArrayList::addAll);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialSearch() {
		try {
			ArrayList<Product> results = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.filter(f -> f.toString().endsWith(".txt"))
					.collect(ArrayList<Product>::new,
							new SerialObjectAccumulator(query),
							ArrayList::addAll);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
}
