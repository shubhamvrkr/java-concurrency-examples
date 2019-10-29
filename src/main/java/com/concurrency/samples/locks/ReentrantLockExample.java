package com.concurrency.samples.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This lock can provide fairness which can be used to provide lock to longest waiting thread.
 * Cons:
 *  - When thread is trying to acquire a lock, it is not made to wait forever if unable to acquire lock (i.e tryLock() with timeout). It has a ability to trying for lock interruptibly, and with timeout
 *  - Preference can be specified to determine which thread should be given priority during acquiring lock. e.g FCFS
 *  - Thread can interrupt other thread which has locked the resource by calling lockInterruptibly when it is waiting for lock
 *  - Provides method to get list of thread waiting for the lock
 */
public class ReentrantLockExample {

    public static void main(String[] args){

        //Resource which the threads are planning to use
        ReentrantLock resource = new ReentrantLock(true);

        // Thread A acquiring lock and waiting untill it obtains lock and then proceeding further
        Thread A = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Thread A started running");
                System.out.println("Thread A acquring lock on resource");
                resource.lock();
                System.out.println("Thread A obtained lock");
                System.out.println("Thread A performing other task dependent on resource");
                try {
                    Thread.sleep(3000);
                    System.out.println("Thread A releasing lock");
                    resource.unlock();
                    System.out.println("Thread A done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //Thread B trying to obtain lock within 1000 millisec and then proceeding no matter whether it obtains lock or not
        Thread B = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread B started running");
                System.out.println("Thread B trying to acquire lock on resource within 1000 millisec ");
                try{
                    boolean locked = resource.tryLock(1000,TimeUnit.MILLISECONDS);
                    if(locked){
                        System.out.println("Thread B obtained lock after waiting for 1000 millisec");
                    }else{
                        System.out.println("Thread B could not obtain lock after waiting for 1000 millisec");
                    }
                    System.out.println("Thread B performing other task independent on resource");
                    Thread.sleep(3000);
                    if(locked){
                        System.out.println("Thread B releasing lock");
                        resource.unlock();
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        // Thread C acquiring lock and waiting until it obtains lock and then proceeding further same as Thread A but however thread can be interrupted
        Thread C = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread C started running");
                System.out.println("Thread C acquring lock on resource");
                try {
                    resource.lockInterruptibly();
                    System.out.println("Thread C obtained lock");
                    System.out.println("Thread C performing other task dependent on resource");
                    Thread.sleep(3000);
                    System.out.println("Thread C releasing lock");
                    resource.unlock();
                    System.out.println("Thread C done");
                } catch (InterruptedException e) {
                    System.out.println("Thread C interrupted");
                }
            }
        });

        A.start();
        B.start();
        C.start();
        //Main thread is interrupting this thread which is waiting for the lock
        C.interrupt();
    }

}
