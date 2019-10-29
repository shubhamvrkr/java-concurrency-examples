package com.concurrency.samples.chapter4.callableandfutures.example1.serial;

import com.concurrency.samples.chapter4.callableandfutures.example1.distance.LevenshteinDistance;

import java.util.List;

public class ExistSerialCalculation {

	public static boolean existWord(String word, List<String> dictionary) {
		for (String str: dictionary) {
			if (LevenshteinDistance.calculate(word, str)==0) {
				return true;
			}
		}
		return false;
	}
}
