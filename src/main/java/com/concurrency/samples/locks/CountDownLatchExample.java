package com.concurrency.samples.locks;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch is used to make a task wait until certain threads complete.
 * In the below example, main thread is waiting until the 4 threads successfully complete their execution
 * CountDownLatch cannot be reset and reused. Check CyclicBarrier for the same
 */
public class CountDownLatchExample {

    public static void main(String args[]){

        //specify number of threads to wait for
        CountDownLatch countDownLatch = new CountDownLatch(4);
        //Task 1
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Worker 1 executing task 1");
                    Thread.sleep(1000);
                    System.out.println("Worker 1 executed task 1");
                    //decrement latch count down
                    countDownLatch.countDown();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        //Task 2
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Worker 2 executing task 2");
                    Thread.sleep(2000);
                    System.out.println("Worker 2 executed task 2");
                    countDownLatch.countDown();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        //Task 3
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Worker 3 executing task 3");
                    Thread.sleep(3000);
                    System.out.println("Worker 3 executed task 3");
                    countDownLatch.countDown();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        //Task 4
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Worker 4 executing task 4");
                    Thread.sleep(4000);
                    System.out.println("Worker 4 executed task 4");
                    countDownLatch.countDown();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try{
            //make main thread wait until all task have been performed i.e count down count goes 0
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //any task after all the threads have completed come here
        //will always be the last statement to print
        System.out.println("All threads have completed their task");
    }
}