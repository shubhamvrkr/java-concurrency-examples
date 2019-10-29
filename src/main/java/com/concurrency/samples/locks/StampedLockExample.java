package com.concurrency.samples.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * This lock solves the starvation issue of ReentrantReadWrite lock, wherein if multiple threads are reading the data, then single write call will have to wait for all readers to finish their task before writing the data
 * StampLock is made up of stamp and mode. stamp is a token that can be used to validate if the data read is latest or not or can be used to change the mode. i.e read access to write access and vice versa
 * stamp = 0 means failure access
 * StampLock gives access to optimistic read - assuming that nothing changed while reading the data
 * StampLock blocks during acquring the lock even is same thread holds the lock
 * Has Three Modes - Read, Write, OptimisticReading
 * Read/Write works same as that of ReentrantReadWriteLock, However OptimisticReading is nonblocking
 *
 */
public class StampedLockExample {

    //Shared resource thread will be accessing
    static class Resource{

        private Integer a ,b;
        private StampedLock stampedLock = new StampedLock();

        public Resource() {
            a = 0;
            b = 0;
        }

        //acquires read lock on data
        //writer threads will wait for this lock to release
        //Multiple reader can get the access for read
        public int calculateSum() {
            long stamp = stampedLock.readLock();
            if(stamp!=0){
                int sum = a+b;
                stampedLock.unlockRead(stamp);
                return sum;
            }
            return -99;
        }

        //acquires write lock on dat
        //Reader calling calculate sum will have to wait until unlocked
        public long writeData(int a, int b) {
            long stamp = stampedLock.writeLock();
            if(stamp!=0){
                this.a = a;
                this.b = b;
            }
            return stamp;
        }

        //unlock the write lock
        public void unlockWrite(long stamp){
            stampedLock.unlockWrite(stamp);
        }

        //get sum using optimistic way
        //non-blocking
        public int getSum() {
            long stamp = stampedLock.tryOptimisticRead();
            System.out.println("Optimistic stamp: "+stamp);
            int x = this.a;
            int y = this.b;
            //stamp will be 0 since write thread is executed first and currently holding a lock
            //this is required to guarantee the data read is always the one write thread has updated
            if(!stampedLock.validate(stamp)) {
                //use blocking way
                return this.calculateSum();
            }else{
                 return x+y;
            }
        }
    }

    public static void main(String[] args) {

        Resource resource = new Resource();
        //Consider a scenario where in one writer thread is updating the data and two readers are reading the data.
        //One reader is reading in pessimistic way and other is reading using optimistic way

        // Thread A is a writer that writes the data, waits for 5 sec and then releases the lock
        Thread A = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Thread A acquring write lock on resource");
                long stamp = resource.writeData(10, 10);
                if (stamp != 0) {
                    System.out.println("Thread A obtained write lock and inserted data: " + stamp);
                    try {
                        System.out.println("Thread A holding the write lock for 5 sec");
                        Thread.sleep(5000);
                        System.out.println("Thread A releasing write lock");
                        resource.unlockWrite(stamp);
                        System.out.println("Thread A done");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Thread A didnt obtain the write lock as other thread might have obtained read or write lock");
                }
            }
        });

        //Thread B trying to read the data using pessimistic way.
        //Writer thread might get block until this thread reads the data
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread B trying to acquire read lock on resource");
                int sum = resource.calculateSum();
                System.out.println("Thread B obtained read lock and read the data: " + sum);
                System.out.println("Thread B done");
            }
        });

        //Thread C trying to read the data using optimistic way.
        //This thread will read the data even if write thread has not unlocked the data
        Thread C = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread C trying to acquire read lock on resource");
                int sum = resource.getSum();
                System.out.println("Thread C obtained read lock and read the data: " + sum);
                System.out.println("Thread C done");
            }
        });

        //Thread B and C will wait until Thread A released the write lock,
        //Once the write lock is released both the threads can read the value simultaneously
        //If Thread A is started again then it will wait until both the readers releases the lock
        A.start();
        B.start();
        C.start();
    }
}
