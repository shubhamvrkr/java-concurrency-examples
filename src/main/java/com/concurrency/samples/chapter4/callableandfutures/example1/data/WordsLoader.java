package com.concurrency.samples.chapter4.callableandfutures.example1.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordsLoader {

	public static List<String> load(String path) {
		Path file = Paths.get(path);
		List<String> data = new ArrayList<String>();
		try (InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				data.add(line);
			}
		} catch (IOException x) {
			x.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
}
