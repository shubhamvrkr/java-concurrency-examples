package com.concurrency.samples.chapter7.mapreduce.example2.serial;

import com.concurrency.samples.chapter7.mapreduce.example2.common.Token;

import java.util.List;

public class SerialInvertedIndex {

	private List<Token> index;

	public void setIndex(List<Token> index) {
		this.index = index;
	}

	public List<Token> getIndex() {
		return index;
	}

	
	
}
