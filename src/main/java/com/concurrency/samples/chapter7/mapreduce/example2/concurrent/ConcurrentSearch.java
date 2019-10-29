package com.concurrency.samples.chapter7.mapreduce.example2.concurrent;

import com.concurrency.samples.chapter7.mapreduce.example2.common.ContentMapper;
import com.concurrency.samples.chapter7.mapreduce.example2.common.QueryResult;
import com.concurrency.samples.chapter7.mapreduce.example2.common.Token;
import com.concurrency.samples.chapter7.mapreduce.example2.common.Utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class ConcurrentSearch {


	public static void basicSearch(String query[]) throws IOException {

		Path path = Paths.get("chapter7_data/example2/index", "invertedIndex.txt");
		HashSet<String> set = new HashSet<>(Arrays.asList(query));
		//required concurrenthashmap as multiple threads will be modifying the list
		QueryResult results = new QueryResult(new ConcurrentHashMap<>());

		try (Stream<String> invertedIndex = Files.lines(path)) {

			invertedIndex.parallel()
			.filter(line -> set.contains(Utils.getWord(line)))
			.flatMap(ConcurrentSearch::basicMapper)
			.forEach(results::append);

			results
				.getAsList()
				.stream()
				.sorted()
				.limit(100)
				.forEach(System.out::println);

			System.out.println("Basic Search Ok");
		}

	}

	public static void reducedSearch(String query[]) throws IOException {
		Path path = Paths.get("chapter7_data/example2/index", "invertedIndex.txt");
		HashSet<String> set = new HashSet<>(Arrays.asList(query));
		QueryResult results = new QueryResult(new ConcurrentHashMap<>());

		// Version 2
		try (Stream<String> invertedIndex = Files.lines(path)) {

			invertedIndex.parallel()
				.filter(line -> set.contains(Utils.getWord(line)))
				.flatMap(ConcurrentSearch::limitedMapper)
				.forEach(results::append);

			results
				.getAsList()
				.stream()
				.sorted()
				.limit(100)
				.forEach(System.out::println);

			System.out.println("Reduced Search Ok ");
		}

	}

	public static void htmlSearch(String query[], String fileName) throws IOException {
		Path path = Paths.get("chapter7_data/example2/index", "invertedIndex.txt");
		HashSet<String> set = new HashSet<>(Arrays.asList(query));
		QueryResult results = new QueryResult(new ConcurrentHashMap<>());

		try (Stream<String> invertedIndex = Files.lines(path)) {

			invertedIndex.parallel()
				.filter(line -> set.contains(Utils.getWord(line)))
				.flatMap(ConcurrentSearch::limitedMapper)
				.forEach(results::append);

			path = Paths.get("chapter7_data/example2", fileName + "_results.html");
			try (BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {

				fileWriter.write("<HTML>");
				fileWriter.write("<HEAD>");
				fileWriter.write("<TITLE>");
				fileWriter.write("Search Results with Streams");
				fileWriter.write("</TITLE>");
				fileWriter.write("</HEAD>");
				fileWriter.write("<BODY>");
				fileWriter.newLine();

				results.getAsList()
					.stream()
					.sorted()
					.limit(100)
						//handles the logic for finding the words in documenet
					.map(new ContentMapper(query)).forEach(l -> {
						try {
							fileWriter.write(l);
							fileWriter.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
				});

				fileWriter.write("</BODY>");
				fileWriter.write("</HTML>");

			} 

			System.out.println("HTML Search Ok ");
		}

	}

    /**
     * All the above has a problem since the stream is doing i/o operations inside and
     * hence work stealing algorithm is not being used properly
     *
     * @param query
     * @param invertedIndex
     */
	public static void preloadSearch(String[] query, ConcurrentInvertedIndex invertedIndex) {

		HashSet<String> set = new HashSet<>(Arrays.asList(query));
		QueryResult results = new QueryResult(new ConcurrentHashMap<>());

		// Version 4
		invertedIndex.getIndex()
			.parallelStream()
			.filter(token -> set.contains(token.getWord()))
			.forEach(results::append);

		results
			.getAsList()
			.stream()
			.sorted()
			.limit(100)
			.forEach(System.out::println);

		System.out.println("Preload Search Ok.");
	}

	public static void executorSearch(String[] query, ConcurrentInvertedIndex invertedIndex, ForkJoinPool pool) {
		HashSet<String> set = new HashSet<>(Arrays.asList(query));
		QueryResult results = new QueryResult(new ConcurrentHashMap<>());

		pool.submit(() -> {
			invertedIndex.getIndex()
				.parallelStream()
				.filter(token -> set.contains(token.getWord()))
				.forEach(results::append);

			results
				.getAsList()
				.stream()
				.sorted()
				.limit(100)
				.forEach(System.out::println);
		}).join();

		System.out.println("Executor Search Ok.");
	
	}

	public static Stream<Token> basicMapper(String input) {
		ConcurrentLinkedDeque<Token> list = new ConcurrentLinkedDeque();
		String word = Utils.getWord(input);
		Arrays
			.stream(input.split(","))
			.skip(1)
			.parallel()
			.forEach(token -> list.add(new Token(word, token)));

		return list.stream();
	}

	public static Stream<Token> limitedMapper(String input) {
		ConcurrentLinkedDeque<Token> list = new ConcurrentLinkedDeque();
		String word = Utils.getWord(input);

		Arrays.stream(input.split(","))
			.skip(1)
			.limit(100)
			.parallel()
			.forEach(token -> {
				list.add(new Token(word, token));
			});

		return list.stream();
	}
}
