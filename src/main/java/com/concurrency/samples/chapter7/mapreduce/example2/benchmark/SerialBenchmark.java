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

package com.concurrency.samples.chapter7.mapreduce.example2.benchmark;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter7.mapreduce.example2.serial.SerialData;
import com.concurrency.samples.chapter7.mapreduce.example2.serial.SerialFileLoader;
import com.concurrency.samples.chapter7.mapreduce.example2.serial.SerialInvertedIndex;
import com.concurrency.samples.chapter7.mapreduce.example2.serial.SerialSearch;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class SerialBenchmark {

	@Param({"james,bond","gone,with,the,wind","rocky"})
	String string;
	
	private SerialInvertedIndex invertedIndex;
	
	private ForkJoinPool pool;
	
	@Setup
	public void init() {
		SerialFileLoader loader = new SerialFileLoader();
		try {
			invertedIndex = loader.load(Paths.get("chapter7_data/example2/index","invertedIndex.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pool = new ForkJoinPool();
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void basicSearch() {
		
		String[] query=string.split(",");
		
		try {
			SerialSearch.basicSearch(query);
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
    public void reducedSearch() {
		
		String[] query=string.split(",");
		
		try {
			SerialSearch.reducedSearch(query);
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
    public void htmlSearch() {
		
		String[] query=string.split(",");
		
		try {
			SerialSearch.htmlSearch(query,query[0]);
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
    public void preloadSearch() {
		String[] query=string.split(",");
		SerialSearch.preloadSearch(query, invertedIndex);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void executorSearch() {
		String[] query=string.split(",");
		SerialSearch.executorSearch(query, invertedIndex, pool);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getWordsInFile1() {
		SerialData.getWordsInFile1("27759897.txt", invertedIndex);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getWordsInFile2() {
		SerialData.getWordsInFile2("27759897.txt", invertedIndex);
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getAverageTfxidf() {
		SerialData.getAverageTfxidf("27759897.txt",invertedIndex);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void maxTfxidf() {
		SerialData.maxTfxidf(invertedIndex);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void minTfxidf() {
		SerialData.minTfxidf(invertedIndex);
	}
}
