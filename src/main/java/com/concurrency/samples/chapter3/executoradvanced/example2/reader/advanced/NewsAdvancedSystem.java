package com.concurrency.samples.chapter3.executoradvanced.example2.reader.advanced;

import com.concurrency.samples.chapter3.executoradvanced.example2.buffer.NewsBuffer;
import com.concurrency.samples.chapter3.executoradvanced.example2.reader.basic.NewsTask;
import com.concurrency.samples.chapter3.executoradvanced.example2.writer.NewsWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This is the main class of the advanced example.
 * @author author
 *
 */
public class NewsAdvancedSystem implements Runnable {

	/**
	 * The route of the file with the list of the RSS feeds
	 */
	private String route;

	/**
	 * Custom executor
	 */
	private NewsExecutor executor;
	
	/**
	 * The buffer that store every item until its written to disk
	 */
	private NewsBuffer buffer;

	/**
	 * Control the finalization of the system
	 */
	private CountDownLatch latch=new CountDownLatch(1);
	
	/**
	 * Constructor of the class
	 * @param route
	 */
	public NewsAdvancedSystem(String route) {
		this.route = route;
		executor = new NewsExecutor(Runtime.getRuntime().availableProcessors());
		buffer=new NewsBuffer();
	}

	/**
	 * This methods read the RSS sources, creates a NewsTask per source and sends them to the Executor
	 */
	@Override
	public void run() {
		Path file = Paths.get(route);
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
				executor.scheduleWithFixedDelay(task,0, 1, TimeUnit.SECONDS);
			}
		} catch (IOException x) {
			x.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		synchronized (this) {
			try {
				wait();
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
