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

package com.concurrency.samples.chapter6.forkandjoin.example3.benchmark;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter6.forkandjoin.example3.common.AmazonMetaData;
import com.concurrency.samples.chapter6.forkandjoin.example3.common.AmazonMetaDataLoader;
import com.concurrency.samples.chapter6.forkandjoin.example3.concurrent.ConcurrentMergeSort;
import com.concurrency.samples.chapter6.forkandjoin.example3.serial.SerialMergeSort;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class MyBenchmark {


    private AmazonMetaData[] data;

    @Setup(Level.Invocation)
    public void init() {

        Path path = Paths.get("chapter6_data/example3", "amazon-meta.csv");
        data = AmazonMetaDataLoader.load(path);

    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialSort() {
        SerialMergeSort mySorter = new SerialMergeSort();
        mySorter.mergeSort(data, 0, data.length);
        System.out.println(data[0].getTitle() + ": " + data[0].getSalesrank());
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentSort() {
        ConcurrentMergeSort mySorter = new ConcurrentMergeSort();
        mySorter.mergeSort(data, 0, data.length);
        System.out.println(data[0].getTitle() + ": " + data[0].getSalesrank());
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialJavaSort() {
        Arrays.sort(data);
        System.out.println(data[0].getTitle() + ": " + data[0].getSalesrank());
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentJavaSort() {
        Arrays.parallelSort(data);
        System.out.println(data[0].getTitle() + ": " + data[0].getSalesrank());

    }

}
