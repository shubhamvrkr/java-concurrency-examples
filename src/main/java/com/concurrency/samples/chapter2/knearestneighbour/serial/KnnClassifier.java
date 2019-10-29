package com.concurrency.samples.chapter2.knearestneighbour.serial;

import com.concurrency.samples.chapter2.knearestneighbour.data.Distance;
import com.concurrency.samples.chapter2.knearestneighbour.data.Sample;
import com.concurrency.samples.chapter2.knearestneighbour.distances.EuclideanDistanceCalculator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Serial implementation of the Knn algorithm
 */
public class KnnClassifier {

	/**
	 * List of train data
	 */
	private List<? extends Sample> dataSet;
	
	/**
	 * K parameter
	 */
	private int k;
	
	/**
	 * Constructor of the class. Initialize the internal data
	 * @param dataSet Train data
	 * @param k K parameter
	 */
	public KnnClassifier(List<? extends Sample> dataSet, int k) {
		this.dataSet=dataSet;
		this.k=k;
	}
	
	/**
	 * Method that classifies an example
	 * @param example Example to classify
	 * @return The tag or class of the example
	 * @throws Exception Exception if something goes wrong
	 */
	public String classify (Sample example) {
		
		Distance[] distances=new Distance[dataSet.size()];
		
		int index=0;
		
		for (Sample localExample : dataSet) {
			distances[index]=new Distance();
			distances[index].setIndex(index);
			distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
			index++;
		}

		Arrays.sort(distances);
		
		Map<String, Integer> results = new HashMap<>();
		for (int i = 0; i < k; i++) {
		  Sample localExample = dataSet.get(distances[i].getIndex());
		  String tag = localExample.getTag();
		  results.merge(tag, 1, (a, b) -> a+b);
		}

		return Collections.max(results.entrySet(),
			    Map.Entry.comparingByValue()).getKey();
	}
}
