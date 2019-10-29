package com.concurrency.samples.chapter5.phaseradvanced.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class DataLoader {

    private static final Pattern WHITESPACE = Pattern.compile(",");

    //get distance matrix from the file
	public static int[][] load(Path path) throws IOException {

		return Files.readAllLines(path)
					.stream()
					.map(line -> WHITESPACE.splitAsStream(line.trim()).mapToInt(Integer::parseInt)
					.toArray())
					.toArray(int[][]::new);
	}

}
