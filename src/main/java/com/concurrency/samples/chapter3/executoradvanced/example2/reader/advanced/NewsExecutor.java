package com.concurrency.samples.chapter3.executoradvanced.example2.reader.advanced;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This is the overriden ScheduledThreadPoolExecutor used in the advanced example
 * @author author
 *
 */
public class NewsExecutor extends ScheduledThreadPoolExecutor {

	/**
	 * Constructor of the class
	 * @param corePoolSize
	 */
	public NewsExecutor(int corePoolSize) {
		super(corePoolSize);
	}

	/**
	 * Override the decorateTask to change the task executed by the executor
	 */
	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable,
			RunnableScheduledFuture<V> task) {
		ExecutorTask<V> myTask = new ExecutorTask<V>(runnable, null, task, this);
		return myTask;
	}
}
