package com.concurrency.samples.chapter6.forkandjoin.example2.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusData;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.FilterData;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ConcurrentSearch {

	public static CensusData findAny(CensusData[] data, List<FilterData> filters, int size) {

		//
		TaskManager manager = new TaskManager();
		//create a task to process the data
		IndividualTask task = new IndividualTask(data, 0, data.length, manager, size, filters);
		ForkJoinPool.commonPool().execute(task);
		try {
			//get the result
			//CPU is not blockcing, current thread uses work stealing algorithm and processes other pending task
			CensusData result = task.join();
			if (result != null) {
				System.out.println("Find First Result: " + result.getCitizenship());
			}

			return result;
		} catch (Exception e) {
			System.err.println("findFirst has finished with an error: " + task.getException().getMessage());
		}
		return null;
	}

	public static List<CensusData> findAll(CensusData[] data, List<FilterData> filters, int size) {
		List<CensusData> results;

		TaskManager manager = new TaskManager();
		ListTask task = new ListTask(data, 0, data.length, manager, size, filters);
		ForkJoinPool.commonPool().execute(task);

		try {
			results = task.join();
			return results;
		} catch (Exception e) {
			System.err.println("findFirst has finished with an error: " + task.getException().getMessage());
		}
		return null;

	}

}
