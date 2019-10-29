package com.concurrency.samples.chapter4.callableandfutures.example2.serial;

import com.concurrency.samples.chapter4.callableandfutures.example2.common.DocumentParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerialIndexing {

	public static void main(String[] args) {

		Date start, end;

		File source = new File("chapter4_data/example2");
		File[] files = source.listFiles();
		Map<String, List<String>> invertedIndex = new HashMap<String, List<String>>();

		start = new Date();
		for (File file : files) {

			DocumentParser parser = new DocumentParser();

			if (file.getName().endsWith(".txt")) {
				Map<String, Integer> voc = parser.parse(file.getAbsolutePath());
				updateInvertedIndex(voc, invertedIndex, file.getName());
			}
		}
		end = new Date();
		System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
		System.out.println("invertedIndex: " + invertedIndex.size());

	}

	private static void updateInvertedIndex(Map<String, Integer> voc, Map<String, List<String>> invertedIndex,
			String fileName) {
		for (String word : voc.keySet()) {
			if (word.length() >= 3) {
				invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(fileName);
			}
		}
	}

}
