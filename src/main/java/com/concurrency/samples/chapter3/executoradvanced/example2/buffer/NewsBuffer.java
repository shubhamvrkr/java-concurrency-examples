package com.concurrency.samples.chapter3.executoradvanced.example2.buffer;

import com.concurrency.samples.chapter3.executoradvanced.example2.data.CommonInformationItem;

import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class implements a buffer of news items. The NewsTask store the items here if they haven't been
 * processed yet and the NewsWriter task will write them to disk
 *
 * @author author
 */
public class NewsBuffer {

    /**
     * The buffer is implemented using a LinkedBlockingQueue
     */
    private LinkedBlockingQueue<CommonInformationItem> buffer;

    /**
     * We use a ConcurrentHashMap to store the ids of every news we have stored into disk. We don't
     * want to store the news more than once.
     */
    private ConcurrentHashMap<String, String> storedItems;

    /**
     * Constructor of the class. Creates the objects used by the class
     */
    public NewsBuffer() {
        buffer = new LinkedBlockingQueue<>();
        storedItems = new ConcurrentHashMap<String, String>();
    }

    /**
     * Add an item to the buffer. First, we check if the item has been stored before using
     * the storedItems hashTable.
     *
     * @param item The item to store in the buffer
     */
    public void add(CommonInformationItem item) {
        storedItems.compute(item.getId(), (id, oldSource) -> {
            if (oldSource == null) {
                buffer.add(item);
                return item.getSource();
            } else {
                System.out.println("Item " + item.getId() + " has been processed before");
                return oldSource;
            }
        });
    }

    /**
     * This method returs the next item to save to disk
     *
     * @return The next item to save to disk
     * @throws InterruptedException
     */
    public CommonInformationItem get() throws InterruptedException {
        return buffer.take();
    }
}
