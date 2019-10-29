package com.concurrency.samples.chapter7.mapreduce.example1.serial.data;

import com.concurrency.samples.chapter7.mapreduce.example1.common.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class SerialDataLoader {

	public static List<Record> load(Path path) throws IOException {
		System.out.println("Loading data");
		//reads all lines and keeps in memory
		//use readLine one by one using stream is file is large
		List<String> lines = Files.readAllLines(path);

		List<Record> records = lines
				//create a serial stream
				.stream()
				//skip the file item as they are headers
				.skip(1)
				//use intermediate operation to alter the stream
				.map(l -> l.split(";"))
				//creates a new stream of list of stream returned from previous stream
				.map(t -> new Record(t))
				//use terminal operation to convert to list
				.collect(Collectors.toList());

		return records;
	}
}
