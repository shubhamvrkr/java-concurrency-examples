package com.concurrency.samples.chapter6.forkandjoin.example2.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusData;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.Filter;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.FilterData;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.RecursiveTask;

/**
 * Task to find any item from the list and return
 */
public class IndividualTask extends RecursiveTask<CensusData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4106884399809219741L;

	private CensusData[] data;
	private int start, end, size;
	private TaskManager manager;
	private List<FilterData> filters;

	public IndividualTask(CensusData[] data, int start, int end, TaskManager manager, int size,
			List<FilterData> filters) {
		this.data = data;
		this.start = start;
		this.end = end;
		this.manager = manager;
		this.size = size;
		this.filters = filters;
	}

	@Override
	protected CensusData compute() {
		//if size less than or equal to threashold, dont divide futher and start executing
		if (end - start <= size) {
			for (int i = start; i < end && !Thread.currentThread().isInterrupted(); i++) {
				CensusData censusData = data[i];
				if (Filter.filter(censusData, filters)) {
					System.out.println("Found: " + i);
					//if found cancel task
					manager.cancelTasks(this);
					return censusData;
				}
			}
			return null;
		} else {
			//divide into two tasks
			int mid = (start + end) / 2;
			IndividualTask task1 = new IndividualTask(data, start, mid, manager, size, filters);
			IndividualTask task2 = new IndividualTask(data, mid, end, manager, size, filters);
			manager.addTask(task1);
			manager.addTask(task2);
			//remove this task
			manager.deleteTask(this);
			//send the task to fork join pool
			task1.fork();
			task2.fork();
			//wait to their finalization. doesnt throw expection if the task are cancelled
            // whereas join method launches and exception is thrown if task is being cancelled
			task1.quietlyJoin();
			task2.quietlyJoin();
			//delete sub task from list of task from manager after complete
			try {
				CensusData res = task1.join();
				if (res != null)
					return res;
				manager.deleteTask(task1);
			} catch (CancellationException ex) {
			}
			try {
				CensusData res = task2.join();
				if (res != null)
					return res;
				manager.deleteTask(task2);
			} catch (CancellationException ex) {
			}
			return null;
		}
	}

}
