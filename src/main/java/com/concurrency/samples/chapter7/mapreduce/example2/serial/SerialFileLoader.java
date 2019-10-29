package com.concurrency.samples.chapter7.mapreduce.example2.serial;

import com.concurrency.samples.chapter7.mapreduce.example2.common.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class SerialFileLoader {

	public SerialInvertedIndex load(Path path) throws IOException {
		SerialInvertedIndex invertedIndex = new SerialInvertedIndex();
		List<Token> results=new ArrayList<>();
		
		try (Stream<String> fileStream = Files.lines(path)) {
			fileStream
			.flatMap(SerialSearch::limitedMapper)
			.forEach(results::add);
		}
		
		invertedIndex.setIndex(results);
		return invertedIndex;
	}
}
