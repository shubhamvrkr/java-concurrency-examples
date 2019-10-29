package com.concurrency.samples.chapter4.callableandfutures.example1.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example1.common.BestMatchingData;
import com.concurrency.samples.chapter4.callableandfutures.example1.data.WordsLoader;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BestMatchingConcurrentMain {

    public static void main(String[] args) {
        try {
            Date startTime, endTime;
            List<String> dictionary = WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");

            System.out.println("Dictionary Size: " + dictionary.size());

            startTime = new Date();
            BestMatchingData result;
            String testWord = "";
            result = BestMatchingBasicConcurrentCalculation.getBestMatchingWords(testWord, dictionary);
            List<String> results = result.getWords();
            endTime = new Date();
            System.out.println("Word: " + testWord);
            System.out.println("Minimun distance: " + result.getDistance());
            System.out.println("List of best matching words: " + results.size());
            for (String word : results) {
                System.out.println(word);
            }
            System.out.println("Execution Time: " + (endTime.getTime() - startTime.getTime()));
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
