package com.concurrency.samples.chapter3.executoradvanced.example1.parallel.cache;

import java.util.Date;

/**
 * Class that stores the data of an element included in the cache
 *
 */
public class CacheItem {
	
	/**
	 * Command to execute
	 */
	private String command;
	
	/**
	 * Response of the command
	 */
	private String response;
	
	/**
	 * Creation date of this item
	 */
	private Date creationDate;
	
	/**
	 * Date when this item was last accessed
	 */
	private Date accessDate;
	
	/**
	 * Constructor of the class
	 * @param command Command to execute
	 * @param response Response to that command
	 */
	public CacheItem(String command, String response) {
		creationDate=new Date();
		accessDate=new Date();
		this.command=command;
		this.response=response;
	}

	public String getCommand() {
		return command;
	}

	public String getResponse() {
		return response;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}
	
	

	
	
}
