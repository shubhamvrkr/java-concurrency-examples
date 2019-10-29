package com.concurrency.samples.chapter2.knearestneighbour.main;

import com.concurrency.samples.chapter2.knearestneighbour.benchmark.MyBenchmark;
import com.concurrency.samples.chapter2.knearestneighbour.data.BankMarketing;
import com.concurrency.samples.chapter2.knearestneighbour.loader.BankMarketingLoader;
import com.concurrency.samples.chapter2.knearestneighbour.serial.KnnClassifier;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * Main class that launches the tests using the serial knn with serial sorting
 *
 * @author Usuario
 */
public class SerialMain {

    public static void main(String[] args) {

        BankMarketingLoader loader = new BankMarketingLoader();
        //load bank data from file
        List<BankMarketing> train = loader.load("chapter2_data\\bank.data");
        System.out.println("Train: " + train.size());
        //load data for testing
        List<BankMarketing> test = loader.load("chapter2_data\\bank.test");
        System.out.println("Test: " + test.size());

        double currentTime = 0d;
        int success = 0, mistakes = 0;

        //values of k to try 10,30,50;
        int k = 10;

        success = 0;
        mistakes = 0;

        KnnClassifier classifier = new KnnClassifier(train, k);
        try {
            long start, end;
            start = System.currentTimeMillis();
            for (BankMarketing example : test) {
                String tag = classifier.classify(example);
                if (tag.equals(example.getTag())) {
                    success++;
                } else {
                    mistakes++;
                }
            }
            end = System.currentTimeMillis();
            currentTime = end - start;

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("******************************************");
        System.out.println("Serial Classifier - K: " + k);
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("Execution Time: " + (currentTime / 1000)
                + " seconds.");
        System.out.println("******************************************");

    }

}
