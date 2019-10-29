package com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command;

import java.net.Socket;


/**
 * Concurrent version of the ErrorCommand. It's executed when an unknown command arrives
 *
 */
public class ConcurrentErrorCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentErrorCommand(Socket socket, String[] command) {
		super(socket,command);
		setCacheable(false);
	}
	
	/**
	 * Method that executes the command
	 */
	@Override
	public String execute() {
		return "Unknown command: "+command[0];
	}

}
