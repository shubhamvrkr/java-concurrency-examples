package com.concurrency.samples.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Phaser class enables synchronization between threads which is performing in different phases
 * During creation of phase we need not know the number of threads and can be reusable
 */
public class PhaserExample {

    //worker thread is carrying out the work in two phases.
    // However requirement is that phase 2 should be proceeded only after all thread have completed phase 1,
    // so each thread after completing phase 1 needs to wait for all other threads to complete phase 1 before proceeding to phase 2
    static class Worker implements Runnable{

        private String workerName;
        private Phaser phaser;
        private int waitingTime;

        public Worker(String workerName, Phaser phaser,int waitingTime){
            this.workerName = workerName;
            this.phaser = phaser;
            this.waitingTime = waitingTime;
            //each thread should register to phaser
            phaser.register();
        }

        @Override
        public void run() {
            try {
                System.out.println(workerName+" started executing phase 1");
                Thread.sleep(waitingTime);
                System.out.println(workerName+" started finished phase 1");
                //inform others that phase 1 is completed by this thread
                phaser.arriveAndAwaitAdvance();
                System.out.println(workerName+" started executing phase 2");
                Thread.sleep(waitingTime);
                System.out.println(workerName+" started finished phase 2");
                //inform other threads and deregister from phaser
                phaser.arriveAndDeregister();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Phaser phaser = new Phaser();
        //start 3 threads
        ExecutorService service = Executors.newFixedThreadPool(3);
        for(int i=0;i<3;i++){
            service.submit(new Worker("Worker "+i,phaser,2000));
        }
        service.shutdown();
    }
}
