package com.concurrency.samples.chapter4.callableandfutures.example1.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example1.data.WordsLoader;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExistBasicConcurrenMain {

	public static void main(String[] args) {
		try {
			Date startTime, endTime;
			List<String> dictionary= WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");
			
			System.out.println("Dictionary Size: "+dictionary.size());
			String testWord = "";
			startTime=new Date();
			Boolean result;
			result = ExistBasicConcurrentCalculation.existWord(testWord, dictionary);
			endTime=new Date();
			System.out.println("Word: "+testWord);
			System.out.println("Exist: "+result);
			System.out.println("Execution Time: "+(endTime.getTime()-startTime.getTime()));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
