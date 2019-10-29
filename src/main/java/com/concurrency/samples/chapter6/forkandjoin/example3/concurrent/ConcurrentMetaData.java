package com.concurrency.samples.chapter6.forkandjoin.example3.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example3.common.AmazonMetaData;
import com.concurrency.samples.chapter6.forkandjoin.example3.common.AmazonMetaDataLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

/**
 * Demonstrate the usage of the counter completer class in fork/join framework.
 * This class includes a method to be executed when its child task are finished executing
 */
public class ConcurrentMetaData {

	public static void main(String[] args) {

		for (int j = 0; j < 10; j++) {
			Path path = Paths.get("chapter6_data/example3", "amazon-meta.csv");

			AmazonMetaData data[] = AmazonMetaDataLoader.load(path);

			AmazonMetaData data2[] = Arrays.copyOf(data, data.length);// copy(data);

			Date start, end;

			start = new Date();
			Arrays.parallelSort(data);
			end = new Date();
			System.out.println("Execution Time Java Arrays.parallelSort(): " + (end.getTime() - start.getTime()));

			System.out.println(data[0].getTitle());
			System.out.println(data2[0].getTitle());
			ConcurrentMergeSort mySorter = new ConcurrentMergeSort();
			start = new Date();
			mySorter.mergeSort(data2, 0, data2.length);
			end = new Date();

			System.out.println("Execution Time Java ConcurrentMergeSort: " + (end.getTime() - start.getTime()));

			for (int i = 0; i < data.length; i++) {
				if (data[i].compareTo(data2[i]) != 0) {
					System.err.println("There's a difference is position " + i);
					System.exit(-1);
				}
			}

			System.out.println("Both arrays are equal");
		}
	}
}
