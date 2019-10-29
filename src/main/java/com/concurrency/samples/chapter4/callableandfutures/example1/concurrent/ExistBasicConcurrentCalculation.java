package com.concurrency.samples.chapter4.callableandfutures.example1.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExistBasicConcurrentCalculation {

	public static boolean existWord(String word, List<String> dictionary) throws InterruptedException, ExecutionException {
		int numCores = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numCores);

		int size = dictionary.size();
		int step = size / numCores;
		int startIndex, endIndex;
		List<ExistBasicTask> tasks = new ArrayList<>();

		for (int i = 0; i < numCores; i++) {
			startIndex = i * step;
			if (i == numCores - 1) {
				endIndex = dictionary.size();
			} else {
				endIndex = (i + 1) * step;
			}
			ExistBasicTask task = new ExistBasicTask(startIndex, endIndex, dictionary, word);
			tasks.add(task);
		}
		try {
			//gets the result when one is found and stops other tasks
			Boolean result = executor.invokeAny(tasks);
			return result;
		} catch (ExecutionException e) {
			if (e.getCause() instanceof NoSuchElementException)
				return false;
			throw e;
		} finally {
			executor.shutdown();
		}
	}
}
