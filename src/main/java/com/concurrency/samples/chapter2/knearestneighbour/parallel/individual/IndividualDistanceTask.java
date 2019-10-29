package com.concurrency.samples.chapter2.knearestneighbour.parallel.individual;

import com.concurrency.samples.chapter2.knearestneighbour.data.Distance;
import com.concurrency.samples.chapter2.knearestneighbour.data.Sample;
import com.concurrency.samples.chapter2.knearestneighbour.distances.EuclideanDistanceCalculator;

import java.util.concurrent.CountDownLatch;

/**
 * Task that of the fine-grained concurrent version
 * 
 * @author author
 *
 */
public class IndividualDistanceTask implements Runnable {

	/**
	 * Array of distances
	 */
	private Distance[] distances;

	/**
	 * Index of the example of the train data
	 */
	private int index;

	/**
	 * Example of the train data
	 */
	private Sample localExample;

	/**
	 * Example we want to classify
	 */
	private Sample example;

	/**
	 * Syncrhonization mechanism to control the end of tasks
	 */
	private CountDownLatch endControler;

	/**
	 * Constructor of the class. Initializes the internal data
	 * 
	 * @param distances
	 *            Array of distances
	 * @param index
	 *            Index of the train data
	 * @param localExample
	 *            Example of the train data
	 * @param example
	 *            Example we want to classify
	 * @param endControler
	 *            Synchronization mechanism to control the end of the task
	 */
	public IndividualDistanceTask(Distance[] distances, int index, Sample localExample, Sample example,
			CountDownLatch endControler) {
		this.distances = distances;
		this.index = index;
		this.localExample = localExample;
		this.example = example;
		this.endControler = endControler;
	}

	@Override
	/**
	 * Concurrent task that calculates the distance between the train example
	 * and the example we want to classify
	 */
	public void run() {
		distances[index] = new Distance();
		distances[index].setIndex(index);
		distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
		endControler.countDown();
	}

}
