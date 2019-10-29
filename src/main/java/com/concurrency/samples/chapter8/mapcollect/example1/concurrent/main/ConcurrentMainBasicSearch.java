package com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This approach searches all the files for the word query using parallel streams
 */
public class ConcurrentMainBasicSearch {


	public static void main(String args[]) {
		String query = "Java";
		Path file = Paths.get("chapter8_data/example1");
		try {
			Date start, end;
			start = new Date();
			List<String> results = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.parallel()
					.filter(f -> f.toString().endsWith(".txt"))
					//supplier is passed as new datastructure to every thread
					.collect(ArrayList<String>::new,
							new ConcurrentStringAccumulator(query),
							List::addAll);
			end = new Date();
			System.out.println("Resultados: "+results.size());
			System.out.println("*************");
			results.forEach(System.out::println);
			System.out.println("Execution Time: "+(end.getTime()-start.getTime()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
