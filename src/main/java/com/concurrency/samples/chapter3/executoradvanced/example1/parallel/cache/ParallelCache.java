package com.concurrency.samples.chapter3.executoradvanced.example1.parallel.cache;

import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that implements the core of the cache system
 *
 */
public class ParallelCache {
	
	/**
	 * Hashmap to store the items of the cache
	 */
	private ConcurrentHashMap<String, CacheItem> cache;
	
	/**
	 * Task to clean the cache
	 */
	private CleanCacheTask task;
	
	/**
	 * Thread to execute the clean task
	 */
	private Thread thread;
	
	/**
	 * Constructor of the class
	 */
	public ParallelCache() {
		cache=new ConcurrentHashMap<String, CacheItem>();
		task=new CleanCacheTask(this);
		thread=new Thread(task);
		thread.start();
	}
	
	/**
	 * Method that stores an element in the cache
	 * @param command Command to store
	 * @param response Response to that command
	 */
	public void put(String command, String response) {
		CacheItem item = new CacheItem(command, response);
		cache.put(command, item);
		
	}
	
	/**
	 * Method that returns an element of the cache or null if it doesn't exists
	 * @param command Command to look for in the cache
	 * @return Response of that command
	 */
	public String get (String command) {
		CacheItem item=cache.get(command);
		if (item==null) {
			return null;
		}
		item.setAccessDate(new Date());
		return item.getResponse();
	}
	
	public void cleanCache() {
		Enumeration<String> keys=cache.keys();
		Date revisionDate=new Date();
		
		while (keys.hasMoreElements()) {
			String key=keys.nextElement();
			CacheItem item=cache.get(key);
			if (revisionDate.getTime()-item.getAccessDate().getTime()>600000) {
				cache.remove(key);
			}
		}
	}
	
	/**
	 * Method that shutdown the cache
	 */
	public void shutdown() {
		thread.interrupt();
	}

	/**
	 * Method that retuns the number of items in the cache
	 * @return The number of items in the cache
	 */
	public int getItemNumber() {
		return cache.size();
	}

}
