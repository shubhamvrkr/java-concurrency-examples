package com.concurrency.samples.synchronization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Simple example using synchronized keyword where multiple thread are trying to modify data.
 * Synchronization allows only one thread to access resource at a time.
 * The lock is acquired to the modifyData method, Lock can also be acquired to the entire resource object in the run method of thread using synchronized(resource){}
 * where resource is shared between threads - synchronized block
 * Used when its not necessary to acquire lock on complete method, rather lock can be acquired only on some objects inside the method
 */
public class SimpleSynchronizationExample {

    public static void main(String[] args){

        Resource resource = new Resource();
        //create threads
        ExecutorService service = Executors.newFixedThreadPool(3);
        //execute threads to consume resources
        IntStream.range(0, 1000).forEach(count -> service.submit(resource::modifyData));
        //wait untill all threads completed their task
        try{
            service.awaitTermination(60, TimeUnit.MILLISECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        //value of the resouce should be 1000 every time
        System.out.println("Resouce Value: "+resource.getData());
        System.exit(1);
    }
}

