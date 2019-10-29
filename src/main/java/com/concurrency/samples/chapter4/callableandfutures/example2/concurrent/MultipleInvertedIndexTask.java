package com.concurrency.samples.chapter4.callableandfutures.example2.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example2.common.Document;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MultipleInvertedIndexTask implements Runnable {

	private CompletionService<List<Document>> completionService;
	private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex;
	private boolean finish;

	public MultipleInvertedIndexTask(
			CompletionService<List<Document>> completionService,
			ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex) {
		this.completionService = completionService;
		this.invertedIndex = invertedIndex;
		finish=false;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				try {
					List<Document> documents = completionService.take().get();
					for (Document document : documents) {
						updateInvertedIndex(document.getVoc(), invertedIndex, document.getFileName());
					}
				} catch (InterruptedException e) {
					break;
				}
			}
			while (true) {
				Future<List<Document>> future = completionService.poll();
				if (future == null)
					break;
				List<Document> documents = future.get();
				for (Document document : documents) {
					updateInvertedIndex(document.getVoc(), invertedIndex, document.getFileName());
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void updateInvertedIndex(
			Map<String, Integer> voc,
			ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex,
			String fileName) {

		for (String word : voc.keySet()) {
			if (word.length() >= 3) {
				invertedIndex.computeIfAbsent(word, k -> new ConcurrentLinkedDeque<>()).add(fileName);
			}
		}
	}

	
	

}
