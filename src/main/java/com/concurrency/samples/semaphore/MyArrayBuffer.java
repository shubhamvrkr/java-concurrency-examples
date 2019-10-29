package com.concurrency.samples.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Custom array buffer to store only 5 items max
 */
public class MyArrayBuffer {

    Semaphore semaphore;
    private List<Integer> buffer;

    public MyArrayBuffer(int arraySize){
        semaphore = new Semaphore(arraySize);
        buffer = new ArrayList<Integer>();
    }

    //add item to list
    public void addItem(int a){
        try{
            semaphore.acquire();
            buffer.add(a);
        }catch (InterruptedException e){
            //current thread is interrupted
            e.printStackTrace();
        }
    }

    //remove item from list at particular index
    public int removeItem(int i){
        int a = buffer.remove(i);
        semaphore.release();
        return  a;
    }
}
