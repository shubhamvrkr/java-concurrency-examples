package com.concurrency.samples.semaphore;

import java.util.Random;

/**
 * Custom thread class which tries to insert an element into the custom array buffer , waits for 3000 millisec and then removes the first element from the custom array buffer
   If the array buffer is full, it will wait until some other thread removes at least one element from the array buffer
 */
public class MyThread extends Thread {

    private String threadName;
    private MyArrayBuffer myArrayBuffer;
    private Random random;
    private int sleepTimer = 1000;

    public MyThread(String name, MyArrayBuffer myArrayBuffer) {
        this.threadName = name;
        this.myArrayBuffer = myArrayBuffer;
        this.random = RandomNumberGenerator.getInstance();
    }

    @Override
    public void run() {
        int elem = random.nextInt(50);
        System.out.println("Thread "+threadName + " is trying to add element " + elem);
        this.myArrayBuffer.addItem(elem);
        System.out.println("Thread "+threadName + " added element " + elem);
        System.out.println("Thread "+threadName + " sleeping for " + sleepTimer + " milliseconds");
        try {
            Thread.sleep(sleepTimer);
            System.out.println("Thread "+threadName + " removing index 0");
            int num = this.myArrayBuffer.removeItem(0);
            System.out.println("Thread "+threadName + " removed value " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
