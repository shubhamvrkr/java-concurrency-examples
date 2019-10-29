package com.concurrency.samples.chapter3.executoradvanced.example1.parallel.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class that implements a concurrent logger system
 * @author author
 *
 */
public class Logger {

	/**
	 * Queue to store the log messages
	 */
	private static ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<String>();
	
	/**
	 * Thread to execute the Log Task 
	 */
	private static Thread thread;
	
	/**
	 * Route to the file where we will write the log
	 */
	private static final String ROUTE = "output\\server.log";

	/**
	 * Block of code that initializes the Log task
	 */
	static {
		LogTask task = new LogTask();
		thread = new Thread(task);
		thread.start();
	}

	/**
	 * Method that write a message in the log
	 * @param message Message to write in the log
	 */
	public static void sendMessage(String message) {
		StringWriter writer = new StringWriter();

		writer.write(new Date().toString());
		writer.write(": ");
		writer.write(message);

		logQueue.offer(writer.toString());
	}

	/**
	 * Method that write all the messages in the queue to the file
	 */
	public static void writeLogs() {
		String message;
		Path path = Paths.get(ROUTE);
		try (BufferedWriter fileWriter = Files.newBufferedWriter(path,StandardOpenOption.CREATE,
				StandardOpenOption.APPEND)) {
			while ((message = logQueue.poll()) != null) {
				StringWriter writer = new StringWriter();
				writer.write(new Date().toString());
				writer.write(": ");
				writer.write(message);
				fileWriter.write(writer.toString());
				fileWriter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that clean the file
	 */
	public static void initializeLog() {
		Path path = Paths.get(ROUTE);
		if (Files.exists(path)) {
			try (OutputStream out = Files.newOutputStream(path,
					StandardOpenOption.TRUNCATE_EXISTING)) {

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method that stops the execution of the log system
	 */
	public static void shutdown() {
		writeLogs();
		thread.interrupt();
	}
}
