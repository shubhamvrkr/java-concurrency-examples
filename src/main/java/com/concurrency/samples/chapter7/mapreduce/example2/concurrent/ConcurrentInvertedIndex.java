package com.concurrency.samples.chapter7.mapreduce.example2.concurrent;

import com.concurrency.samples.chapter7.mapreduce.example2.common.Token;

import java.util.List;

public class ConcurrentInvertedIndex {

	private List<Token> index;

	public void setIndex(List<Token> index) {
		this.index = index;
	}

	public List<Token> getIndex() {
		return index;
	}

	
	
}
