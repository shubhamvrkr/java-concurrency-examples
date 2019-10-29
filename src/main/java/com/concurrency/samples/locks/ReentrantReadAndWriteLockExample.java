package com.concurrency.samples.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This lock internally maintains two types of lock. i.e ReadLock and WriteLock.
 * Multiple thread can obtain read locks if there is no thread holding write lock or is waiting for write lock
 * Only one thread can hold write lock if there is no other thread holding a read lock
 * Thread can downgrade write lock to read lock after writing and reading the updated value but a thread with read lock cannot change the mode to write
 */
public class ReentrantReadAndWriteLockExample {

    public static void main(String[] args) {

        Resource<Integer> resource = new Resource<Integer>();
        // Thread A acquiring write lock and inserting items in the array and waiting to perform other operation before releasing the lock
        Thread A = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Thread A acquring write lock on resource");
                resource.acquireWriteLockAndWriteData(10);
                System.out.println("Thread A obtained write lock and inserted data");
                try {
                    System.out.println("Thread A holding the write lock for 3 sec");
                    Thread.sleep(3000);
                    System.out.println("Thread A releasing write lock");
                    resource.releaseWriteLock();
                    System.out.println("Thread A done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Thread B trying to read the data from the list
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread B trying to acquire read lock on resource");
                int a = resource.acquireReadLockAndReadData(0);
                System.out.println("Thread B obtained read lock and read the data: " + a);
                try {
                    System.out.println("Thread B holding the read lock for 5 sec");
                    Thread.sleep(5000);
                    System.out.println("Thread B released read lock");
                    resource.releaseReadLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Thread C trying to read the data from the list
        Thread C = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread C trying to acquire read lock on resource");
                int a = resource.acquireReadLockAndReadData(0);
                System.out.println("Thread C obtained read lock and read the data: " + a);
                try {
                    System.out.println("Thread C holding the read lock for 5 sec");
                    Thread.sleep(5000);
                    System.out.println("Thread C released read lock");
                    resource.releaseReadLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Thread B and C will wait until Thread A released the write lock,
        // Once the write lock is released both the threads can read the value simultaneously
        //If Thread A is started again then it will wait untill both the readers releases the lock
        A.start();
        B.start();
        C.start();
        try {
            A.join();
            B.join();
            C.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //main thread is locking the resource and reading the array length //should be 1
        System.out.println("List Length: " + resource.readLength());
    }

    //Shared resource thread will be accessing
    static class Resource<E> {

        private List<E> dataList;
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

        public Resource() {
            dataList = new ArrayList<E>();
        }

        //acquires read lock on data but doesnt releases it (for testing flow)
        //usually should release the lock after reading the data
        public E acquireReadLockAndReadData(int index) {
            readLock.lock();
            return dataList.get(index);
        }

        //releases the lock on resource
        public void releaseReadLock() {
            readLock.unlock();
        }

        //acquires write lock on data but doesnt releases it (for testing flow)
        //usually should release the lock after writing the data
        public void acquireWriteLockAndWriteData(E elem) {
            writeLock.lock();
            dataList.add(elem);
        }

        //releases the lock on resource
        public void releaseWriteLock() {
            writeLock.unlock();
        }

        //get length of the list
        public int readLength() {
            readLock.lock();
            int size = dataList.size();
            readLock.unlock();
            return size;
        }

    }
}
