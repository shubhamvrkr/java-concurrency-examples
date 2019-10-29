package com.concurrency.samples.chapter2.knearestneighbour.parallel.group;

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
 * Coarse-grained concurrent version of the Knn algorithm
 * @author author
 *
 */
public class KnnClassifierParallelGroup {

	/**
	 * Train data
	 */
	private List<? extends Sample> dataSet;
	
	/**
	 * K parameter
	 */
	private int k;
	
	/**
	 * Executor to execute the concurrent tasks
	 */
	private ThreadPoolExecutor executor;
	
	/**
	 * Number of threads to configure the executor
	 */
	private int numThreads;
	
	/**
	 * Check to indicate if we use the serial or the parallel sorting
	 */
	private boolean parallelSort;
	
	/**
	 * Constructor of the class. Initialize the internal data
	 * @param dataSet Train data set
	 * @param k K parameter
	 * @param factor Factor of increment of the number of cores
	 * @param parallelSort Check to indicate if we use the serial or the parallel sorting
	 */
	public KnnClassifierParallelGroup(List<? extends Sample> dataSet, int k, int factor, boolean parallelSort) {
		this.dataSet=dataSet;
		this.k=k;
		numThreads=factor*(Runtime.getRuntime().availableProcessors());
		executor=(ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
		this.parallelSort=parallelSort;
	}
	
	/**
	 * Method that classify an example
	 * @param example Example to classify
	 * @return Class or tag of the example
	 * @throws Exception Exception if something goes wrong
	 */
	public String classify (Sample example) throws Exception {
		
		Distance[] distances=new Distance[dataSet.size()];
		CountDownLatch endControler=new CountDownLatch(numThreads);
		
		int length=dataSet.size()/numThreads;
		int startIndex=0, endIndex=length;
		
		for (int i=0; i <numThreads; i++) {
			GroupDistanceTask task=new GroupDistanceTask(distances, startIndex, endIndex, dataSet, example, endControler);
			startIndex=endIndex;
			if (i<numThreads-2) {
				endIndex=endIndex+length; 
			} else {
				endIndex=dataSet.size();
			}
			executor.execute(task);
			
		}
		endControler.await();

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
