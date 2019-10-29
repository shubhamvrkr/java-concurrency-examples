package com.concurrency.samples.chapter3.executoradvanced.example2.reader.basic;

import com.concurrency.samples.chapter3.executoradvanced.example2.buffer.NewsBuffer;
import com.concurrency.samples.chapter3.executoradvanced.example2.writer.NewsWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is the core class of the News example
 * @author author
 *
 */
public class NewsSystem implements Runnable {

	/**
	 * Route to the file with the list of RSS sources
	 */
	private String route;

	/**
	 * Executor for the tasks.
	 * Allows task to be executed periodically or after a delay.
	 */
	private ScheduledThreadPoolExecutor executor;
	
	/**
	 * Buffer to store the news
	 */
	private NewsBuffer buffer;
	
	/**
	 * Control the finalization of the system
	 */
	private CountDownLatch latch=new CountDownLatch(1);

	
	/**
	 * Constructor of the class
	 * @param route Route to the file with the RSS sources
	 */
	public NewsSystem(String route) {
		this.route = route;
		//creates executor with thread count = available cpu cores
		executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
		buffer=new NewsBuffer();
	}

	/**
	 * This method runs the list of RSS sources, creates a task per source and sends
	 * them to the Executor
	 */
	@Override
	public void run() {
		Path file = Paths.get(route);
		//creates one independent thread for writing news in buffer
		NewsWriter newsWriter=new NewsWriter(buffer);
		Thread t=new Thread(newsWriter);
		t.start();
		
		try (InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(";");

				NewsTask task = new NewsTask(data[0], data[1], buffer);
				System.out.println("Task "+task.getName());
				//important function to execute task in fixed delay
				executor.scheduleWithFixedDelay(task,0, 1, TimeUnit.MINUTES);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		synchronized (this) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Shutting down the executor.");
		executor.shutdown();
		t.interrupt();
		System.out.println("The system has finished.");
	}

	public void shutdown() {
		latch.countDown();
		
	}
}
