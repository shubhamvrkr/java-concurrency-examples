package com.concurrency.samples.chapter3.executoradvanced.example1.parallel.log;

import java.util.concurrent.TimeUnit;

/**
 * Task that writes all the messages in the log file every ten seconds.
 * It implements the Runnable interface. It will be executed as a thread
 * @author author
 *
 */
public class LogTask implements Runnable {

	@Override
	/**
	 * Main method of the task
	 */
	public void run() {
		try {
			while (true) {
				TimeUnit.SECONDS.sleep(10);
				Logger.writeLogs();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
