package com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command;

import com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.server.ConcurrentServer;

import java.net.Socket;

/**
 * Class that implements the concurrent version of the Stop command.
 * Stops the server
 *
 */
public class ConcurrentStopCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentStopCommand (Socket socket, String [] command) {
		super (socket, command);
		setCacheable(false);
	}
	
	/**
	 * Method that executes the command
	 */
	@Override
	public String execute() {
		ConcurrentServer.shutdown();
		return "Server stopped";
	}

}
