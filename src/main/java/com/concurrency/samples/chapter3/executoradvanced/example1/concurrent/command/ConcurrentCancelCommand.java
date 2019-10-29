package com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.command;

import com.concurrency.samples.chapter3.executoradvanced.example1.concurrent.server.ConcurrentServer;
import com.concurrency.samples.chapter3.executoradvanced.example1.parallel.log.Logger;

import java.net.Socket;

/**
 * Concurrent command that cancel the tasks of a user
 *
 */
public class ConcurrentCancelCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class. It receives the socket to communicate with the client and the parameters of the command
	 * @param socket Socket to communicate with the client
	 * @param command Parameters of the command
	 */
	public ConcurrentCancelCommand(Socket socket, String[] command) {
		super(socket, command);
		setCacheable(false);
	}

	/**
	 * Main method of the command. Cancel the tasks of the client and return the message to write to the client
	 */
	@Override
	public String execute() {
		ConcurrentServer.cancelTasks(getUsername());
		String message = "Tasks of user "
				+getUsername()
				+" has been cancelled.";
		Logger.sendMessage(message);
		return message;
	}

}
