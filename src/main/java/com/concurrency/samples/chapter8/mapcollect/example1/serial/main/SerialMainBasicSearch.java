package com.concurrency.samples.chapter8.mapcollect.example1.serial.main;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class SerialMainBasicSearch {
	
	public static void main(String args[]) {
		String query = "Java";
		Path file=Paths.get("chapter8_data/example1");
		try {
		Date start, end;
		
		start=new Date();
		ArrayList<String> results=Files.walk(file ,FileVisitOption.FOLLOW_LINKS).filter(f -> f.toString().endsWith(".txt")).collect(
						ArrayList<String>::new,
						//using string accumulator instead of object
						new SerialStringAccumulator(query),
						ArrayList::addAll);
		end=new Date();
		System.out.println("Resultados");
		System.out.println("*************");
		results.forEach(f -> System.out.println(f));
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
