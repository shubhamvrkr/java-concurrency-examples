package com.concurrency.samples.chapter3.executoradvanced.example1.serial.command;

import com.concurrency.samples.chapter3.executoradvanced.example1.common.Command;

/**
 * Class that implements the serial version of the Stop command. 
 * Finish the execution of the server
 *
 */
public class StopCommand extends Command {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public StopCommand (String [] command) {
		super (command);
	}
	
	@Override
	/**
	 * Method that executes the command
	 */
	public String execute() {
		return "Server stopped";
	}

}
