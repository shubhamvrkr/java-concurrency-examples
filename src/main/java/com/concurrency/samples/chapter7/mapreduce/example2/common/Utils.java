package com.concurrency.samples.chapter7.mapreduce.example2.common;

public class Utils {

	public static String getWord(String w) {
		int pos = w.indexOf(":");
		String word = w.substring(0, pos);
		return word;
	}

}
