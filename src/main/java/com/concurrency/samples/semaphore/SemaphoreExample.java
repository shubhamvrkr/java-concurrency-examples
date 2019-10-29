package com.concurrency.samples.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Consider a producer-consumer scenario where is you have an custom array buffer which can only store up to 5 items.
 * If any producers is trying to put an item in custom array buffer which is full,
 * it should wait until some items are consumed by the consumers from the custom array buffer
 * Advantage of using semaphore over synzhronized:
 *  - semaphore allows to configure no of threads (i.e n ) to access the resources

  //Output:
    First 5 thread should be allowed to insert data in the array buffer,
    After which they will wait for 1 min,
    On resuming they will remove the first element from the array
    Upon which other threads will be able to store items in the array

 */
public class SemaphoreExample {

    public static void main(String[] args) {
        String[]threadNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"};
        MyArrayBuffer myArrayBuffer = new MyArrayBuffer(5);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNames.length);
        for (int i = 0; i < threadNames.length; i++) {
            executorService.execute(new MyThread(threadNames[i], myArrayBuffer));
        }
    }
}
