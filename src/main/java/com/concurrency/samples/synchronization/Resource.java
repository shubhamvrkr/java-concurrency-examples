package com.concurrency.samples.synchronization;

public class Resource {

    //this is the resource the threads are trying to take control to modify
    private int data = 0;

    //operation to modify the data
    //provides the lock only to this method, so make sure there is no other method modifying the data which the thread can call
    public synchronized void modifyData(){
        data++;
    }

    //read the data
    public int getData() {
        return data;
    }
}
