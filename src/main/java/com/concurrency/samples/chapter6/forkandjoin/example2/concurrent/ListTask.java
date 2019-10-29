package com.concurrency.samples.chapter6.forkandjoin.example2.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusData;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.Filter;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.FilterData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.RecursiveTask;

public class ListTask extends RecursiveTask<List<CensusData>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4106884399809219741L;

	private CensusData data[];
	private int start, end, size;
	private List<FilterData> filters;
	private TaskManager manager;

	public ListTask(CensusData data[], int start, int end, TaskManager manager, int size, List<FilterData> filters) {
		this.data = data;
		this.start = start;
		this.end = end;
		this.size = size;
		this.filters = filters;
		this.manager = manager;
	}

	@Override
	protected List<CensusData> compute() {
		List<CensusData> ret = new ArrayList<CensusData>();
		if (end - start <= size) {
			for (int i = start; i < end; i++) {
				CensusData censusData = data[i];
				if (Filter.filter(censusData, filters)) {
					ret.add(censusData);
				}
			}
		} else {
			int mid = (start + end) / 2;
			ListTask task1 = new ListTask(data, start, mid, manager, size, filters);
			ListTask task2 = new ListTask(data, mid, end, manager, size, filters);
			manager.addTask(task1);
			manager.addTask(task2);
			//this actual task cant be cancelled but the child task can be cancelled,
            // so delete this from to be cancelled list
			manager.deleteTask(this);
			task1.fork();
			task2.fork();
			task2.quietlyJoin();
			task1.quietlyJoin();
			try {
				List<CensusData> tmp = task1.join();
				if (tmp != null)
					ret.addAll(tmp);
				manager.deleteTask(task1);
			} catch (CancellationException ex) {
			}
			try {
				List<CensusData> tmp = task2.join();
				if (tmp != null)
					ret.addAll(tmp);
				manager.deleteTask(task2);
			} catch (CancellationException ex) {
			}
		}
		return ret;
	}

}
