package com.concurrency.samples.chapter4.callableandfutures.example2.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example2.common.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultipleConcurrentIndexing {

	public static void main(String[] args) {

		int numCores=Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(Math.max(numCores-1, 1));
		ExecutorCompletionService<List<Document>> completionService=new ExecutorCompletionService<>(executor);
		ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex=new ConcurrentHashMap<String,ConcurrentLinkedDeque<String>> ();
		final int NUMBER_OF_TASKS=Integer.parseInt(args[0]);
		Date start, end;

		File source = new File("data");
		File[] files = source.listFiles();
		
		start=new Date();
		List<File> taskFiles=new ArrayList<>();
		for (File file : files) {
			taskFiles.add(file);
			if (taskFiles.size()==NUMBER_OF_TASKS) {
				MultipleIndexingTask task=new MultipleIndexingTask(taskFiles);
				completionService.submit(task);
				taskFiles=new ArrayList<>();
			}
		}
		if (taskFiles.size()>0) {
			MultipleIndexingTask task=new MultipleIndexingTask(taskFiles);
			completionService.submit(task);
		}
		
		MultipleInvertedIndexTask invertedIndexTask=new MultipleInvertedIndexTask(completionService,invertedIndex);
		Thread thread1=new Thread(invertedIndexTask);
		thread1.start();
		MultipleInvertedIndexTask invertedIndexTask2=new MultipleInvertedIndexTask(completionService,invertedIndex);
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
		thread1.interrupt();
		thread2.interrupt();

	}

}
