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

package com.concurrency.samples.chapter4.callableandfutures.example2.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter4.callableandfutures.example2.common.Document;
import com.concurrency.samples.chapter4.callableandfutures.example2.common.DocumentParser;
import com.concurrency.samples.chapter4.callableandfutures.example2.concurrent.IndexingTask;
import com.concurrency.samples.chapter4.callableandfutures.example2.concurrent.InvertedIndexTask;
import com.concurrency.samples.chapter4.callableandfutures.example2.concurrent.MultipleIndexingTask;
import com.concurrency.samples.chapter4.callableandfutures.example2.concurrent.MultipleInvertedIndexTask;
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
	
	@Param({"100","1000","5000"})
	private int numDocuments;

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialIndexing() {

		File source = new File("data");
		File[] files = source.listFiles();
		Map<String, List<String>> invertedIndex = new HashMap<String, List<String>>();

		for (File file : files) {

			DocumentParser parser = new DocumentParser();

			if (file.getName().endsWith(".txt")) {
				Map<String, Integer> voc = parser.parse(file.getAbsolutePath());
				updateInvertedIndex(voc, invertedIndex, file.getName());
			}
		}
		System.out.println("invertedIndex: " + invertedIndex.size());
	}
	
	private void updateInvertedIndex(Map<String, Integer> voc, Map<String, List<String>> invertedIndex,
			String fileName) {
		for (String word : voc.keySet()) {
			if (word.length() >= 3) {
				invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(fileName);
			}
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentIndexing() {
		int numCores=Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(Math.max(numCores-1, 1));
		ExecutorCompletionService<Document> completionService=new ExecutorCompletionService<>(executor);
		ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex=new ConcurrentHashMap<String,ConcurrentLinkedDeque<String>> ();

		File source = new File("data");
		File[] files = source.listFiles();
		
		for (File file : files) {
			IndexingTask task=new IndexingTask(file);
			completionService.submit(task);
		}
		
		InvertedIndexTask invertedIndexTask=new InvertedIndexTask(completionService,invertedIndex);
		Thread thread1=new Thread(invertedIndexTask);
		thread1.start();
		InvertedIndexTask invertedIndexTask2=new InvertedIndexTask(completionService,invertedIndex);
		Thread thread2=new Thread(invertedIndexTask2);
		thread2.start();
		
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
			thread1.interrupt();
			thread2.interrupt();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("invertedIndex: "+invertedIndex.size());		
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void multipleConcurrentIndexing() {
		int numCores=Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(Math.max(numCores-1, 1));
		ExecutorCompletionService<List<Document>> completionService=new ExecutorCompletionService<>(executor);
		ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex=new ConcurrentHashMap<String,ConcurrentLinkedDeque<String>> ();
		final int NUMBER_OF_TASKS=numDocuments;

		File source = new File("data");
		File[] files = source.listFiles();
		
		List<File> taskFiles=new ArrayList<>();
		for (File file : files) {
			taskFiles.add(file);
			if (taskFiles.size()==NUMBER_OF_TASKS) {
				MultipleIndexingTask task=new MultipleIndexingTask(taskFiles);
				completionService.submit(task);
				taskFiles=new ArrayList<>();
			}
		}
		if (taskFiles.size()>0) {
			MultipleIndexingTask task=new MultipleIndexingTask(taskFiles);
			completionService.submit(task);
		}
		
		MultipleInvertedIndexTask invertedIndexTask=new MultipleInvertedIndexTask(completionService,invertedIndex);
		Thread thread1=new Thread(invertedIndexTask);
		thread1.start();
		MultipleInvertedIndexTask invertedIndexTask2=new MultipleInvertedIndexTask(completionService,invertedIndex);
		Thread thread2=new Thread(invertedIndexTask2);
		thread2.start();
		
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
			thread1.interrupt();
			thread2.interrupt();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("invertedIndex: "+invertedIndex.size());
		thread1.interrupt();
		thread2.interrupt();		
	}

}
