package com.concurrency.samples.semaphore;

import javax.management.monitor.Monitor;
import java.util.Random;

/**
 * Singleton class to generate only one instance for random class
 */
public class RandomNumberGenerator {
    private static class RandomGenerator{
        private static final Random INSTANCE = new Random();
    }
    public static Random getInstance(){
        return  RandomGenerator.INSTANCE;
    }
}
