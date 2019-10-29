package com.concurrency.samples.chapter3.executoradvanced.example2.core;

import com.concurrency.samples.chapter3.executoradvanced.example2.reader.basic.NewsSystem;

import java.util.concurrent.TimeUnit;

/**
 * Main class of the basic example.
 * Example using scheduledpoolexecutor
 * @author author
 * This class demonstrate providing custom implementation to ScheduledThreadPoolExecutor for managing the delay of execution of task
 */
public class Main {

	public static void main(String[] args) {

		// Creates the System an execute it as a Thread
		NewsSystem system=new NewsSystem("chapter3_data\\example2\\sources.txt");
		
		Thread t=new Thread(system);
		
		t.start();
		
		// Waits 10 minutes
		try {
			TimeUnit.MINUTES.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Shutsdown the system
		system.shutdown();

	}

}
