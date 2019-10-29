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

package com.concurrency.samples.chapter2.knearestneighbour.benchmark;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.concurrency.samples.chapter2.knearestneighbour.data.BankMarketing;
import com.concurrency.samples.chapter2.knearestneighbour.loader.BankMarketingLoader;
import com.concurrency.samples.chapter2.knearestneighbour.parallel.group.KnnClassifierParallelGroup;
import com.concurrency.samples.chapter2.knearestneighbour.parallel.individual.KnnClassifierParallelIndividual;
import com.concurrency.samples.chapter2.knearestneighbour.serial.KnnClassifier;
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

    @Param({"10", "30", "50"})
    public int size;

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialMain() {
        BankMarketingLoader loader = new BankMarketingLoader();
        List<BankMarketing> train = loader.load("data\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = loader.load("data\\bank.test");
        System.out.println("Test: " + test.size());
        int success = 0, mistakes = 0;
        int k = size;

        success = 0;
        mistakes = 0;
        KnnClassifier classifier = new KnnClassifier(train, k);
        try {

            for (BankMarketing example : test) {
                String tag = classifier.classify(example);
                if (tag.equals(example.getTag())) {
                    success++;
                } else {
                    mistakes++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("******************************************");
        System.out.println("Serial Classifier - K: " + k);
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("******************************************");
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void parallelIndividualMain() {
        BankMarketingLoader loader = new BankMarketingLoader();
        List<BankMarketing> train = loader.load("data\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = loader.load("data\\bank.test");
        System.out.println("Test: " + test.size());
        int success = 0, mistakes = 0;
        int k = size;
        success = 0;
        mistakes = 0;
        KnnClassifierParallelIndividual classifier = new KnnClassifierParallelIndividual(train, k, 1, false);
        try {
            for (BankMarketing example : test) {
                String tag = classifier.classify(example);
                if (tag.equals(example.getTag())) {
                    success++;
                } else {
                    mistakes++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        classifier.destroy();
        System.out.println("******************************************");
        System.out.println("Parallel Classifier Individual - K: " + k + " - Factor 1 - Parallel Sort: false");
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("******************************************");

    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void parallelIndividualMainSort() {
        BankMarketingLoader loader = new BankMarketingLoader();
        List<BankMarketing> train = loader.load("data\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = loader.load("data\\bank.test");
        System.out.println("Test: " + test.size());
        int success = 0, mistakes = 0;
        int k = size;
        success = 0;
        mistakes = 0;
        KnnClassifierParallelIndividual classifier = new KnnClassifierParallelIndividual(train, k, 1, true);
        try {
            for (BankMarketing example : test) {
                String tag = classifier.classify(example);
                if (tag.equals(example.getTag())) {
                    success++;
                } else {
                    mistakes++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        classifier.destroy();
        System.out.println("******************************************");
        System.out.println("Parallel Classifier Individual - K: " + k + " - Factor 1 - Parallel Sort: true");
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("******************************************");

    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void parallelGroupMain() {
        BankMarketingLoader loader = new BankMarketingLoader();
        List<BankMarketing> train = loader.load("data\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = loader.load("data\\bank.test");
        System.out.println("Test: " + test.size());
        int success = 0, mistakes = 0;
        int k = size;

        success = 0;
        mistakes = 0;
        KnnClassifierParallelGroup classifier = new KnnClassifierParallelGroup(train, k, 1, false);
        try {
            for (BankMarketing example : test) {
                String tag = classifier.classify(example);
                if (tag.equals(example.getTag())) {
                    success++;
                } else {
                    mistakes++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        classifier.destroy();

        System.out.println("******************************************");
        System.out.println("Parallel Classifier Group - K: " + k + " - Factor 1 - Parallel Sort: false");
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("******************************************");
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(1)
    @Warmup(iterations = 10, time = 1, batchSize = 1)
    @Measurement(iterations = 10, time = 1, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void parallelGroupMainSort() {
        BankMarketingLoader loader = new BankMarketingLoader();
        List<BankMarketing> train = loader.load("data\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = loader.load("data\\bank.test");
        System.out.println("Test: " + test.size());
        int success = 0, mistakes = 0;
        int k = size;

        success = 0;
        mistakes = 0;
        KnnClassifierParallelGroup classifier = new KnnClassifierParallelGroup(train, k, 1, true);
        try {
            for (BankMarketing example : test) {
                String tag = classifier.classify(example);
                if (tag.equals(example.getTag())) {
                    success++;
                } else {
                    mistakes++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        classifier.destroy();

        System.out.println("******************************************");
        System.out.println("Parallel Classifier Group - K: " + k + " - Factor 1 - Parallel Sort: true");
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("******************************************");

    }

}
