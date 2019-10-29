package com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.server;

import com.concurrency.samples.chapter3.executoradvanced.example1.common.Constants;
import com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command.ConcurrentCancelCommand;
import com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command.ConcurrentCommand;
import com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.executor.ServerTask;
import com.concurrency.samples.chapter3.executoradvanced.example1.parallel.cache.ParallelCache;
import com.concurrency.samples.chapter3.executoradvanced.example1.parallel.log.Logger;
import com.concurrency.samples.chapter3.executoradvanced.example1.wdi.data.WDIDAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Class that implements the concurrent server
 *
 */
public class ConcurrentServer {

	/**
	 * Cache to get a better performance
	 */
	private static ParallelCache cache;

	/**
	 * Attribute to control the status of the server
	 */
	private static volatile boolean stopped = false;

	/**
	 * Method that store the pending request the server has to process
	 */
	private static LinkedBlockingQueue<Socket> pendingConnections;

	/**
	 * Hashsmap that stores the Future instances of the tasks executed by the
	 * server. We use this Hashmap to cancel the tasks
	 */
	private static ConcurrentMap<String, ConcurrentMap<ConcurrentCommand, ServerTask<?>>> taskController;

	/**
	 * Thread that will convert the requests to commands
	 */
	private static Thread requestThread;

	/**
	 * The Runnable that convert the requests to commands
	 */
	private static RequestTask task;
	
	/**
	 * Socket to accept the server requests
	 */
	private static ServerSocket serverSocket; 
	
	/**
	 * Main method that implements the core functionality of the server
	 * 
	 * @param args
	 *            Arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		WDIDAO dao = WDIDAO.getDAO();
		cache = new ParallelCache();
		Logger.initializeLog();
		pendingConnections = new LinkedBlockingQueue<Socket>();
		taskController = new ConcurrentHashMap<String, ConcurrentMap<ConcurrentCommand, ServerTask<?>>>();
		task = new RequestTask(pendingConnections, taskController);
		requestThread = new Thread(task);
		requestThread.start();

		System.out.println("Initialization completed.");

		serverSocket = new ServerSocket(Constants.CONCURRENT_PORT);
		//this thread puts the newly connection in the pending connection queue.
		do {
			try {
				Socket clientSocket = serverSocket.accept();
				pendingConnections.put(clientSocket);
			} catch (IOException e) {
				// No action required
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!stopped);
		finishServer();
		System.out.println("Shutting down cache");
		cache.shutdown();
		System.out.println("Cache ok" + new Date());

	}

	/**
	 * Method that returns the cache
	 * 
	 * @return The cache
	 */
	public static ParallelCache getCache() {
		return cache;
	}

	/**
	 * Methods that finish the execution of the server
	 */
	public static void shutdown() {
		stopped = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method finish the last elements of the server
	 */
	private static void finishServer() {
		System.out.println("Shutting down the server...");
		task.shutdown();
		task.terminate();
		System.out.println("Shutting down Request task");
		requestThread.interrupt();
		System.out.println("Request task ok");
		System.out.println("Closing socket");
		System.out.println("Shutting down logger");
		Logger.sendMessage("Shuttingdown the logger");
		Logger.shutdown();
		System.out.println("Logger ok");
		System.out.println("Main server thread ended");
	}

	/**
	 * This method is executed to cancel all the tasks of a user
	 * 
	 * @param username
	 *            Name of the user
	 */
	public static void cancelTasks(String username) {

		ConcurrentMap<ConcurrentCommand, ServerTask<?>> userTasks = taskController.get(username);
		if (userTasks == null) {
			return;
		}
		int taskNumber = 0;
		Iterator<ServerTask<?>> it = userTasks.values().iterator();
		while (it.hasNext()) {
			ServerTask<?> task = it.next();
			ConcurrentCommand command = task.getCommand();
			if (!(command instanceof ConcurrentCancelCommand) && task.cancel(true)) {
				taskNumber++;
				Logger.sendMessage(
						"Task with code " + command.hashCode() + "cancelled: " + command.getClass().getSimpleName());
				it.remove();
			}
		}
		String message = taskNumber + " tasks has been cancelled.";
		Logger.sendMessage(message);

	}

	/**
	 * This method is used to delete a finished tasks of the Hashmap with the
	 * futures that controls the execution of the tasks
	 * 
	 * @param username
	 *            Name of the user
	 */
	public static void finishTask(String username, ConcurrentCommand command) {

		ConcurrentMap<ConcurrentCommand, ServerTask<?>> userTasks = taskController.get(username);

		userTasks.remove(command);

		String message = "Task with code " + command.hashCode() + " has finished";
		Logger.sendMessage(message);

	}

}
