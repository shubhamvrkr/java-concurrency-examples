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

package com.concurrency.samples.chapter6.forkandjoin.example1.benchmark;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.concurrency.samples.chapter6.forkandjoin.example1.common.VocabularyLoader;
import com.concurrency.samples.chapter6.forkandjoin.example1.concurrent.ConcurrentKMeans;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.Document;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.DocumentCluster;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.DocumentLoader;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.SerialKMeans;
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
public class MyBenchmark {

	@Param({ "5", "10", "15", "20" })
	int K;
	@Param({ "1", "13" })
	int SEED;
	@Param({ "1", "20", "400" })
	int MAX_SIZE;

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void serialKMeans() {
		try {
			Path pathVoc = Paths.get("chapter6_data/example1", "movies.words");
			Map<String, Integer> vocIndex = VocabularyLoader.load(pathVoc);
			System.out.println("Voc Size: " + vocIndex.size());

			Path pathDocs = Paths.get("chapter6_data/example1", "movies.data");
			Document[] documents = DocumentLoader.load(pathDocs, vocIndex);
			System.out.println("Document Size: " + documents.length);

			Date start, end;
			start = new Date();
			DocumentCluster[] clusters = SerialKMeans.calculate(documents, K, vocIndex.size(), SEED);
			end = new Date();
			System.out.println("K: " + K + "; SEED: " + SEED);
			System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

			System.out.println(
					Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
							.map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));

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
	public void concurrentKMeans() {
		try {
			Path pathVoc = Paths.get("chapter6_data/example1\\movies.words");
			Map<String, Integer> vocIndex = VocabularyLoader.load(pathVoc);
			System.out.println("Voc Size: " + vocIndex.size());

			Path pathDocs = Paths.get("chapter6_data/example1\\movies.data");
			Document[] documents = DocumentLoader.load(pathDocs, vocIndex);
			System.out.println("Document Size: " + documents.length);

			Date start, end;
			start = new Date();
			DocumentCluster[] clusters = ConcurrentKMeans.calculate(documents, K, vocIndex.size(), SEED, MAX_SIZE);
			end = new Date();
			System.out.println("K: " + K + "; SEED: " + SEED + "; MAX_SIZE: " + MAX_SIZE);
			System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

			System.out.println(
					Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
							.map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
