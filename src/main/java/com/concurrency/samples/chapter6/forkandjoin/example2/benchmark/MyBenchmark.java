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

package com.concurrency.samples.chapter6.forkandjoin.example2.benchmark;

import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusData;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusDataLoader;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.FilterData;
import com.concurrency.samples.chapter6.forkandjoin.example2.concurrent.ConcurrentSearch;
import com.concurrency.samples.chapter6.forkandjoin.example2.serial.SerialSearch;
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
public class MyBenchmark {
	
	private CensusData[] data;
	
	@Param({"10","200","2000" })
	int SIZE;
	
	@Setup
	public void init() {
		Path path = Paths.get("chapter6_data/example2","census-income.data");

		data = CensusDataLoader.load(path);
		System.out.println("Number of items: " + data.length);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialTest1() {
		// Test 1: findFirst, exists, in the first ones
		List<FilterData> filters = new ArrayList<>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);

		CensusData result = SerialSearch.findAny(data, filters);
		System.out.println("Test 1 - Result: "
				+ result.getReasonForUnemployment());
    }

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialTest2() {
		// Test 2: findFirst, exists, in the last ones
		List<FilterData> filters = new ArrayList<>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("United-States");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Greece");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Private");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("62");
		filters.add(filter);

		CensusData result = SerialSearch.findAny(data, filters);
		System.out.println("Test 2 - Result: "
				+ result.getReasonForUnemployment());
		
    }
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialTest3() {
		// Test 3 Doesn't exists
		List<FilterData> filters = new ArrayList<>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("XXXX");
		filters.add(filter);

		CensusData result = SerialSearch.findAny(data, filters);
		if (result == null) {
			System.out.println("Test 3 - Result: " + result);
		} else {
			System.out.println("Test 3 - Result: "
					+ result.getReasonForUnemployment());
		}
    }
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialTest4() {
		// Test 4: Error Individual
		List<FilterData> filters = new ArrayList<>();
		FilterData filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("Dominican-Republic");
		filters.add(filter);

		try {
			CensusData result = SerialSearch.findAny(data, filters);
			System.out.println("Test 4 - Results: " + result.getCitizenship());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

    }

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialTest5() {
		// Test 5: List
		List<FilterData> filters = new ArrayList<>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);

		List<CensusData> results = SerialSearch.findAll(data, filters);
		System.out.println("Test 5 - Results: " + results.size());
		
    }
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialTest6() {
		// Test 6: Error List
		List<FilterData> filters = new ArrayList<>();
		FilterData filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("Dominican-Republic");
		filters.add(filter);

		try {
			List<CensusData> results = SerialSearch.findAll(data, filters);
			System.out.println("Test 6 - Results: " + results.size());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
    }

	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentTest1() {
		// Test 1: findFirst, exists, in the first ones
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);

		CensusData result = ConcurrentSearch.findAny(data, filters, SIZE);
		System.out.println("Test 1 - Result: "
				+ result.getReasonForUnemployment());		
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentTest2() {
		// Test 2: findFirst, exists, in the last ones
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("United-States");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Greece");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Private");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("62");
		filters.add(filter);

		CensusData result = ConcurrentSearch.findAny(data, filters, SIZE);
		System.out.println("Test 2 - Result: "
				+ result.getReasonForUnemployment());
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentTest3() {
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("XXXX");
		filters.add(filter);

		CensusData result = ConcurrentSearch.findAny(data, filters, SIZE);
		if (result == null) {
			System.out.println("Test 3 - Result: " + result);
		} else {
			System.out.println("Test 3 - Result: "
					+ result.getReasonForUnemployment());
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentTest4() {
		// Test 4: Error find First
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("Dominican-Republic");
		filters.add(filter);

		CensusData result = ConcurrentSearch.findAny(data, filters, SIZE);
		if (result != null) {
			System.out.println("Test 4 - Results: " + result.getCitizenship());
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentTest5() {
		// Test 5: Lista
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);

		List<CensusData> results = ConcurrentSearch.findAll(data, filters, SIZE);
		System.out.println("Test 5 - Results: " + results.size());
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentTest6() {
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("Dominican-Republic");
		filters.add(filter);

		List<CensusData> results = ConcurrentSearch.findAll(data, filters, SIZE);
		if (results != null) {
			System.out.println("Test 6 - Results: " + results.size());
		}
	}

}
