package com.concurrency.samples.chapter4.callableandfutures.example1.serial;

import com.concurrency.samples.chapter4.callableandfutures.example1.common.BestMatchingData;
import com.concurrency.samples.chapter4.callableandfutures.example1.data.WordsLoader;

import java.util.Date;
import java.util.List;


public class BestMatchingSerialMain {

	public static void main(String[] args) {

		Date startTime, endTime;
		List<String> dictionary= WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");
		
		System.out.println("Dictionary Size: "+dictionary.size());
		String testWord = "";
		startTime=new Date();
		BestMatchingData result=BestMatchingSerialCalculation.getBestMatchingWords(testWord, dictionary);
		List<String> results=result.getWords();
		endTime=new Date();
		System.out.println("Word: "+testWord);
		System.out.println("Minimun distance: "+result.getDistance());
		System.out.println("List of best matching words: "+results.size());
		results.forEach(System.out::println);
		System.out.println("Execution Time: "+(endTime.getTime()-startTime.getTime()));
	}

}
