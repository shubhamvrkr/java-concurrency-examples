package com.concurrency.samples.chapter2.knearestneighbour.main;

import com.concurrency.samples.chapter2.knearestneighbour.data.BankMarketing;
import com.concurrency.samples.chapter2.knearestneighbour.loader.BankMarketingLoader;
import com.concurrency.samples.chapter2.knearestneighbour.parallel.group.KnnClassifierParallelGroup;

import java.util.Date;
import java.util.List;

/**
 * Main class that launches the tests using the  coarse-grained concurrent version and concurrent sorting
 * @author author
 *
 */
public class ParallelGroupMainSort {

	public static void main(String[] args) {

		BankMarketingLoader loader = new BankMarketingLoader();
		List<BankMarketing> train = loader.load("chapter2_data\\bank.data");
		System.out.println("Train: " + train.size());
		List<BankMarketing> test = loader.load("chapter2_data\\bank.test");
		System.out.println("Test: " + test.size());
		double currentTime = 0.0d;
		int success = 0, mistakes = 0;
		int k=Integer.parseInt(args[0]);

			success = 0;
			mistakes = 0;
			KnnClassifierParallelGroup classifier = new KnnClassifierParallelGroup(
					train, k, 1, true);
			try {
				Date start, end;
				start = new Date();
				for (BankMarketing example : test) {
					String tag = classifier.classify(example);
					if (tag.equals(example.getTag())) {
						success++;
					} else {
						mistakes++;
					}
				}
				end = new Date();

				currentTime = end.getTime() - start.getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			classifier.destroy();

			System.out.println("******************************************");
			System.out.println("Parallel Classifier Group - K: " + k
					+ " - Factor 1 - Parallel Sort: true");
			System.out.println("Success: " + success);
			System.out.println("Mistakes: " + mistakes);
			System.out.println("Execution Time: " + (currentTime / 1000)
					+ " seconds.");
			System.out.println("******************************************");

	}

}