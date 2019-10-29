package com.concurrency.samples.chapter4.callableandfutures.example2.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example2.common.Document;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrentIndexing {

	public static void main(String[] args) {

		int numCores=Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(Math.max(numCores-1, 1));

		//using invoke all is problem some as we need to store all the future object
		//mechanism to allow decouple production of task and consumption of the result of those task.
		//this will allow us to process the result of finished task parallel
		ExecutorCompletionService<Document> completionService=new ExecutorCompletionService<>(executor);

		ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex=new ConcurrentHashMap<String,ConcurrentLinkedDeque<String>> ();
		
		Date start, end;

		File source = new File("chapter4_data/example2");
		File[] files = source.listFiles();
		
		start=new Date();
		for (File file : files) {
			IndexingTask task=new IndexingTask(file);
			completionService.submit(task);
		}

		//task to poll the result if any task is being completed and handle the processing of the result
		InvertedIndexTask invertedIndexTask=new InvertedIndexTask(completionService,invertedIndex);
		Thread thread1=new Thread(invertedIndexTask);
		thread1.start();
		InvertedIndexTask invertedIndexTask2=new InvertedIndexTask(completionService,invertedIndex);
		Thread thread2=new Thread(invertedIndexTask2);
		thread2.start();
		
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
			thread1.interrupt();
			thread2.interrupt();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end=new Date();
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));
		System.out.println("invertedIndex: "+invertedIndex.size());
	}

}
