package com.concurrency.samples.chapter6.forkandjoin.example2.concurrent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

//Manager to hold the task, required during cancellation of other task
public class TaskManager {

	//hold task, make sure the task are not too many
	private Set<RecursiveTask> tasks;
	private AtomicBoolean cancelled;

	public TaskManager() {
		tasks = ConcurrentHashMap.newKeySet(); 
		cancelled = new AtomicBoolean(false);
	}

	public void addTask(RecursiveTask task) {
		tasks.add(task);
	}

	public void cancelTasks(RecursiveTask sourceTask) {

		if (cancelled.compareAndSet(false, true)) {
			for (RecursiveTask task : tasks) {
				if (task != sourceTask) {
					if(cancelled.get()) {
						task.cancel(true);
					} 
					else {
					    //do not delete the task that has found the solution
						tasks.add(task);					
					}
				}
			}
		}
	}

	public void deleteTask(RecursiveTask task) {
		tasks.remove(task);
	}

}
