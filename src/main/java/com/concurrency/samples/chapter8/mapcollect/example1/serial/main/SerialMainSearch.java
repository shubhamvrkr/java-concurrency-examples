package com.concurrency.samples.chapter8.mapcollect.example1.serial.main;

import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

/**
 * This demonstrate example of using collect in streams framework
 */
public class SerialMainSearch {

	public static void main(String args[]) {
		String query = "Java";
		Path file = Paths.get("chapter8_data/example1");
		try {
			Date start, end;
			start=new Date();
			ArrayList<Product> results = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.filter(f -> f.toString().endsWith(".txt"))
                    //filter out product that contains the word query in its title
					.collect(ArrayList<Product>::new,
							new SerialObjectAccumulator(query),
							ArrayList::addAll);
			end=new Date();
			System.out.println("Resultados");
			System.out.println("*************");
			results.forEach(p -> System.out.println(p.getTitle()));
			System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
