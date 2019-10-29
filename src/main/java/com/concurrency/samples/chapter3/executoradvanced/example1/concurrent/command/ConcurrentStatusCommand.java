package com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command;

import com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.server.ConcurrentServer;
import com.concurrency.samples.chapter3.executoradvanced.example1.parallel.log.Logger;

import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Class that implement the concurrent version of the status command. 
 * Returns information about the executor that executes the concurrent tasks of the
 * server
 *
 */
public class ConcurrentStatusCommand extends ConcurrentCommand {

	private ThreadPoolExecutor executor;
	
	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentStatusCommand (ThreadPoolExecutor executor, Socket socket, String[] command) {
		super(socket, command);
		this.executor=executor;
		setCacheable(false);
	}
	
	/**
	 * Method that executes the command
	 */
	
	@Override
	public String execute() {
		StringBuilder sb=new StringBuilder();
		
		sb.append("Server Status;");
		sb.append("Actived Threads: ");
		sb.append(String.valueOf(executor.getActiveCount()));
		sb.append(";");
		sb.append("Maximum Pool Size: ");
		sb.append(String.valueOf(executor.getMaximumPoolSize()));
		sb.append(";");
		sb.append("Core Pool Size: ");
		sb.append(String.valueOf(executor.getCorePoolSize()));
		sb.append(";");
		sb.append("Pool Size: ");
		sb.append(String.valueOf(executor.getPoolSize()));
		sb.append(";");
		sb.append("Largest Pool Size: ");
		sb.append(String.valueOf(executor.getLargestPoolSize()));
		sb.append(";");
		sb.append("Completed Task Count: ");
		sb.append(String.valueOf(executor.getCompletedTaskCount()));
		sb.append(";");
		sb.append("Task Count: ");
		sb.append(String.valueOf(executor.getTaskCount()));
		sb.append(";");
		sb.append("Queue Size: ");
		sb.append(String.valueOf(executor.getQueue().size()));
		sb.append(";");
		sb.append("Cache Size: ");
		sb.append(String.valueOf(ConcurrentServer.getCache().getItemNumber()));
		sb.append(";");
		Logger.sendMessage(sb.toString());
		return sb.toString();
	}

}
