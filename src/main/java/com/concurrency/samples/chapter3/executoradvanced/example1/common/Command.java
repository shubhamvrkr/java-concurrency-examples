package com.concurrency.samples.chapter3.executoradvanced.example1.common;

/**
 * Abstract class that includes the necessary elements of every Command
 *
 */
public abstract class Command {

	/**
	 * String with all the data of the command: The command itself and its parameters
	 */
	protected String[] command;
	
	/**
	 * Boolean value to indicate that the command is cacheable or not
	 */
	private boolean cacheable;
	
	/**
	 * Constructor of the class
	 * @param command
	 */
	public Command (String [] command) {
		this.command=command;
		cacheable=true;
	}
	
	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	/**
	 * Abstract method that executes the command
	 * @return An String with the response of the command
	 */
	public abstract String execute ();

}
