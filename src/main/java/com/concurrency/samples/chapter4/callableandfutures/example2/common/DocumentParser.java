package com.concurrency.samples.chapter4.callableandfutures.example2.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DocumentParser {
	
	public Map<String, Integer>  parse(String route) {
		Map<String, Integer> ret=new HashMap<String,Integer>();
		Path file=Paths.get(route);
		try (BufferedReader reader = Files.newBufferedReader(file)) {
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	parseLine(line,ret);
			    }
			} catch (IOException x) {
			  x.printStackTrace();
			}
		return ret;
		
	}

	private static final Pattern PATTERN = Pattern.compile("\\P{IsAlphabetic}+");

	private void parseLine(String line, Map<String, Integer> ret) {
	  for(String word: PATTERN.split(line)) {
	    if(!word.isEmpty())
	      ret.merge(Normalizer.normalize(word, Normalizer.Form.NFKD).toLowerCase(), 1, (a, b) -> a+b);
	  }
	}
	
}
