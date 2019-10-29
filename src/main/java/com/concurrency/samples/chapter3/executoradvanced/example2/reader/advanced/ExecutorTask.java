package com.concurrency.samples.chapter3.executoradvanced.example2.reader.advanced;

import com.concurrency.samples.chapter3.executoradvanced.example2.reader.basic.NewsTask;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is the class that implements the tasks the executor will run in the advanced example. These are
 * the real tasks of the executor that runs the Runnable objects you sent using the Executor methods
 * @author author
 *
 * @param <V>
 */
public class ExecutorTask<V> extends FutureTask<V> implements
		RunnableScheduledFuture<V> {

	/**
	 * The original task
	 */
	private RunnableScheduledFuture<V> task;
	
	/**
	 * The executor
	 */
	private NewsExecutor executor;
	
	/**
	 * The start date of the next execution
	 */
	private long startDate;
	
	/**
	 * The name of the RSS feed
	 */
	private String name;
	
	
/**
 * Constructor of the class
 * @param runnable The task sent to the executor
 * @param result Result returned by the task. It will be null
 * @param task Original task
 * @param executor The Exeuctor object
 */
	public ExecutorTask(Runnable runnable, V result,
			RunnableScheduledFuture<V> task,
			NewsExecutor executor) {
		super(runnable, result);
		this.task = task;
		this.executor = executor;
		this.name=((NewsTask)runnable).getName();
		this.startDate=new Date().getTime();
}

	/**
	 * Returns the time to the next execution of the tasks in the given time unit
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		long delay;
		if (!isPeriodic()) {
			delay = task.getDelay(unit);
		} else {
			if (startDate == 0) {
				delay = task.getDelay(unit);
			} else {
				Date now = new Date();
				delay = startDate - now.getTime();
				delay = unit.convert(delay, TimeUnit.MILLISECONDS);
			}

		}
		
		return delay;
	}

	/**
	 * This methods compare two ExecutorTasks based on the start date of the next execution
	 */
	@Override
	public int compareTo(Delayed object) {
		return Long.compare(this.getStartDate(), ((ExecutorTask<V>)object).getStartDate());
	}

	/**
	 * This methods indicate if the task is periodic or not
	 */
	@Override
	public boolean isPeriodic() {
		return task.isPeriodic();
	}

	/**
	 * This method executes the task. We have to calculate the next start date and insert the task in the
	 * queue of the Executor again
	 */
	@Override
	public void run() {
		if (isPeriodic() && (!executor.isShutdown())) {
			super.runAndReset();
			Date now=new Date();
			startDate=now.getTime()+Timer.getPeriod();
			executor.getQueue().add(this);
			System.out.println("Start Date: "+new Date(startDate));
		}
	}

	/**
	 * Returns the name of the RSS feed
	 * @return The name of the RSS feed
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the start date of the next execution
	 * @return The start date of the next execution
	 */
	public long getStartDate() {
		return startDate;
	}
	
	

}
