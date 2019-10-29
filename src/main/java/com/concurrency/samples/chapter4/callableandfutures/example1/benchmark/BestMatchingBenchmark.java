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

package com.concurrency.samples.chapter4.callableandfutures.example1.benchmark;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter4.callableandfutures.example1.common.BestMatchingData;
import com.concurrency.samples.chapter4.callableandfutures.example1.concurrent.BestMatchingAdvancedConcurrentCalculation;
import com.concurrency.samples.chapter4.callableandfutures.example1.concurrent.BestMatchingBasicConcurrentCalculation;
import com.concurrency.samples.chapter4.callableandfutures.example1.data.WordsLoader;
import com.concurrency.samples.chapter4.callableandfutures.example1.serial.BestMatchingSerialCalculation;
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
public class BestMatchingBenchmark {
	
	@Param({"stitter","abicus","lonx"})
	private String bestWord;

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void bestMatchingSerialMain() {
		List<String> dictionary=WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");
		
		System.out.println("Dictionary Size: "+dictionary.size());
		
		BestMatchingData result= BestMatchingSerialCalculation.getBestMatchingWords(bestWord, dictionary);
		List<String> results=result.getWords();
		System.out.println("Word: "+bestWord);
		System.out.println("Minimun distance: "+result.getDistance());
		System.out.println("List of best matching words: "+results.size());
		for (String word: results) {
			System.out.println(word);
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void bestMatchingConcurrentMain() {
		try {
			List<String> dictionary= WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");
			
			System.out.println("Dictionary Size: "+dictionary.size());
			
			BestMatchingData result;
			result = BestMatchingBasicConcurrentCalculation.getBestMatchingWords(bestWord, dictionary);
			List<String> results=result.getWords();
			System.out.println("Word: "+bestWord);
			System.out.println("Minimun distance: "+result.getDistance());
			System.out.println("List of best matching words: "+results.size());
			for (String word: results) {
				System.out.println(word);
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void bestMatchingConcurrentAdvancedMain() {
		try {
			List<String> dictionary=WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");
			
			System.out.println("Dictionary Size: "+dictionary.size());
			
			BestMatchingData result;
			result = BestMatchingAdvancedConcurrentCalculation.getBestMatchingWords(bestWord, dictionary);
			List<String> results=result.getWords();
			System.out.println("Word: "+bestWord);
			System.out.println("Minimun distance: "+result.getDistance());
			System.out.println("List of best matching words: "+results.size());
			for (String word: results) {
				System.out.println(word);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}
