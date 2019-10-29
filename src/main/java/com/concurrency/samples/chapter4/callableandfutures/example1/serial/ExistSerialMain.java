package com.concurrency.samples.chapter4.callableandfutures.example1.serial;

import com.concurrency.samples.chapter4.callableandfutures.example1.data.WordsLoader;

import java.util.Date;
import java.util.List;


public class ExistSerialMain {

	public static void main(String[] args) {

		Date startTime, endTime;
		List<String> dictionary= WordsLoader.load("chapter4_data/example1/UK Advanced Cryptics Dictionary.txt");
		
		System.out.println("Dictionary Size: "+dictionary.size());

		String testWord = "";
		startTime=new Date();
		boolean result=ExistSerialCalculation.existWord(testWord, dictionary);
		endTime=new Date();
		
		System.out.println("Word: "+testWord);
		System.out.println("Exists: "+result);
		System.out.println("Execution Time: "+(endTime.getTime()-startTime.getTime()));
	}

}
