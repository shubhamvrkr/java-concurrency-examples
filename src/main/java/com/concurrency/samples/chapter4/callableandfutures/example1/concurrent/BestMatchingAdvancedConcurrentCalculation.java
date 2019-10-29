package com.concurrency.samples.chapter4.callableandfutures.example1.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example1.common.BestMatchingData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class BestMatchingAdvancedConcurrentCalculation {

	public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) throws InterruptedException, ExecutionException {

		int numCores = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numCores);

		int size = dictionary.size();
		int step = size / numCores;
		int startIndex, endIndex;
		List<Future<BestMatchingData>> results;
		List<BestMatchingBasicTask> tasks = new ArrayList<>();

		for (int i = 0; i < numCores; i++) {
			startIndex = i * step;
			if (i == numCores - 1) {
				endIndex = dictionary.size();
			} else {
				endIndex = (i + 1) * step;
			}
			BestMatchingBasicTask task = new BestMatchingBasicTask(startIndex, endIndex, dictionary, word);
			tasks.add(task);
		}

		//return when all tasks are executed
		results = executor.invokeAll(tasks);
		executor.shutdown();
		
		List<String> words = new ArrayList<String>();
		int minDistance = Integer.MAX_VALUE;
		for (Future<BestMatchingData> future : results) {
			BestMatchingData data = future.get();
			if (data.getDistance() < minDistance) {
				words.clear();
				minDistance = data.getDistance();
				words.addAll(data.getWords());
			} else if (data.getDistance() == minDistance) {
				words.addAll(data.getWords());
			}
		}

		BestMatchingData result = new BestMatchingData();
		result.setDistance(minDistance);
		result.setWords(words);
		return result;

	}

}
