package com.concurrency.samples.locks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier can be used in cases where one thread needs to wait for another thread to reach its execution up to certain point
 * before executing further
 * CyclicBarrier allows all threads to arrive at specific point
 * CyclicBarrier can be reset and reused unlike CountDownLatch
 */
public class CyclicBarrierExample {


    public static void main(String args[]){
        //Similar example like CountDownLatch using CyclicBarrier
        cyclicBarrierSimpleExample();
    }

    private static void cyclicBarrierSimpleExample(){
        //specify number of threads to wait for
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("All task have been completed");
            }
        });

        //Task 1
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Worker 1 executing task 1");
                    Thread.sleep(3000);
                    System.out.println("Worker 1 executed task 1");
                    cyclicBarrier.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (BrokenBarrierException e){
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
                    Thread.sleep(3000);
                    System.out.println("Worker 2 executed task 2");
                    cyclicBarrier.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (BrokenBarrierException e){
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
                    cyclicBarrier.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (BrokenBarrierException e){
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
                    Thread.sleep(3000);
                    System.out.println("Worker 4 executed task 4");
                    cyclicBarrier.await();
                    cyclicBarrier.reset();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

}
