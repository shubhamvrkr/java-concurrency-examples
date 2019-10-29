package com.concurrency.samples.chapter6.forkandjoin.example3.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example3.serial.SerialMergeSort;

import java.util.concurrent.CountedCompleter;

public class MergeSortTask extends CountedCompleter<Void> {

	private static final long serialVersionUID = -5183127767439978300L;
	private Comparable[] data;
	private int start, end;
	private int middle;

	public MergeSortTask(Comparable[] data, int start, int end, MergeSortTask parent) {
		super(parent);

		this.data = data;
		this.start = start;
		this.end = end;
	}

	@Override
	public void compute() {
		if (end - start >= 1024) {
			middle = (end + start) >>> 1;
			MergeSortTask task1 = new MergeSortTask(data, start, middle, this);
			MergeSortTask task2 = new MergeSortTask(data, middle, end, this);
			addToPendingCount(2);
			//add tasks to the fork/join common pool
			task1.fork();
			task2.fork();

		} else {
			new SerialMergeSort().mergeSort(data, start, end);
			//this method internally calls the onCompletion method.
			tryComplete();
		}
	}

	/**
	 * Method is called on completing the child task
	 * Responsible for merging the list
	 * @param caller
	 */
	@Override
	public void onCompletion(CountedCompleter<?> caller) {

		if (middle == 0) {
			return;
		}
		int length = end - start + 1;
		Comparable tmp[] = new Comparable[length];

		int i, j, index;
		i = start;
		j = middle;
		index = 0;

		while ((i < middle) && (j < end)) {
			if (data[i].compareTo(data[j]) <= 0) {
				tmp[index] = data[i];
				i++;
			} else {
				tmp[index] = data[j];
				j++;
			}
			index++;
		}

		while (i < middle) {
			tmp[index] = data[i];
			i++;
			index++;
		}

		while (j < end) {
			tmp[index] = data[j];
			j++;
			index++;
		}

		for (index = 0; index < (end - start); index++) {
			data[index + start] = tmp[index];
		}

	}

}
