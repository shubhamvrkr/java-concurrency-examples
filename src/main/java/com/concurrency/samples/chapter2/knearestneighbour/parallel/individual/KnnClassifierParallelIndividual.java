package com.concurrency.samples.chapter2.knearestneighbour.parallel.individual;

import com.concurrency.samples.chapter2.knearestneighbour.data.Distance;
import com.concurrency.samples.chapter2.knearestneighbour.data.Sample;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Fine-grained concurrent version of the Knn algorithm
 * @author author
 *
 */
public class KnnClassifierParallelIndividual {

	/**
	 * Train data
	 */
	private List<? extends Sample> dataSet;
	
	/**
	 * K parameter
	 */
	private int k;
	
	/**
	 * Executor to 
	 */
	private ThreadPoolExecutor executor;
	
	/**
	 * Number of threads the executor will use internally
	 */
	private int numThreads;
	
	/**
	 * Mark that indicates if we use the serial or parallel sorting
	 */
	private boolean parallelSort;
	
	/**
	 * Constructor of the class. Initializes the internal parameters
	 * @param dataSet Train data sest
	 * @param k K parameter
	 * @param factor Factor of increment of the internal number of cores
	 * @param parallelSort Mark that indicates if we use the serial or parallel sorting
	 */
	public KnnClassifierParallelIndividual(List<? extends Sample> dataSet, int k, int factor, boolean parallelSort) {
		this.dataSet=dataSet;
		this.k=k;
		numThreads=factor*(Runtime.getRuntime().availableProcessors());
		executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
		this.parallelSort=parallelSort;
	}
	
	/**
	 * Method that classifies and example
	 * @param example Example to classify
	 * @return The tag or class of the example
	 * @throws Exception Exception is something goes wrong
	 */
	public String classify (Sample example) throws Exception {
		
		Distance[] distances=new Distance[dataSet.size()];
		CountDownLatch endController=new CountDownLatch(dataSet.size());
		
		int index=0;
		for (Sample localExample : dataSet) {
			IndividualDistanceTask task=new IndividualDistanceTask(distances, index, localExample, example, endController);
			executor.execute(task);
			index++;
		}
		endController.await();
		
		if (parallelSort) {
			Arrays.parallelSort(distances);
		} else {
			Arrays.sort(distances);
		}
		
		Hashtable<String, Integer> results=new Hashtable<>();
		for (int i=0; i<k; i++) {
			Sample localExample=dataSet.get(distances[i].getIndex());
			String tag=localExample.getTag();
			Integer counter=results.get(tag);
			if (counter==null) {
				counter=new Integer(1);
				results.put(tag, counter);
			} else {
				counter=counter++;
			}
		}
		
		
		Enumeration<String> keys=results.keys();
		int max=0;
		String result=null;
		while (keys.hasMoreElements()) {
			String key=keys.nextElement();
			int value=results.get(key);
			if (value>max) {
				max=value;
				result=key;
			}
		}

		return result;
	}
	
	/**
	 * Method that finish the execution of the executor
	 */
	public void destroy() {
		executor.shutdown();
	}
	
}
