package com.concurrency.samples.chapter4.callableandfutures.example1.concurrent;

import com.concurrency.samples.chapter4.callableandfutures.example1.distance.LevenshteinDistance;

import java.util.List;
import java.util.concurrent.Callable;


public class ExistBasicTask implements Callable<Boolean> {

	private int startIndex;
	
	private int endIndex;
	
	private List<String> dictionary;
	
	private String word;
	
	public ExistBasicTask(int startIndex, int endIndex, List<String> dictionary, String word) {
		this.startIndex=startIndex;
		this.endIndex=endIndex;
		this.dictionary=dictionary;
		this.word=word;
	}

	@Override
	public Boolean call() throws Exception {
		for (int i=startIndex; i<endIndex; i++) {
			if (LevenshteinDistance.calculate(word, dictionary.get(i))==0) {
				return true;
			}
			
			if (Thread.interrupted()) {
				return false;
			}
		}
		throw new Exception("The word "+word+" doesn't exists.");
	}

}
