package com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ConcurrentStringAccumulator implements BiConsumer<List<String>, Path> {

	private String word;

	public ConcurrentStringAccumulator(String word) {
		this.word = word.toLowerCase();
	}

	@Override
	public void accept(List<String> list, Path path) {
		boolean result;
		try (Stream<String> lines = Files.lines(path)) {
			result = lines
					.parallel()
					.map(l -> l.split(":")[1].toLowerCase())
					.anyMatch(l -> l.contains(word));
			if (result) {
				list.add(path.toString());
			}
		} catch (Exception e) {
			System.out.println(path);
			e.printStackTrace();
		}
	}

}
