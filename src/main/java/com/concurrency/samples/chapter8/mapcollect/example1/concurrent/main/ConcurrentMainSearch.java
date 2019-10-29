package com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main;

import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This example demonstrate use of collect function in streams framework;
 * Collect has two version:
 * 1 - Supplier (created new one for each thread in parallel mode),Accumulator(BiConsumer),Combiner (BiConsumer)
 * 2 - Supplier (created new one for each thread in parallel mode),Accumulator(BiConsumer),Combiner(BinaryOperator),Finisher,Characteristic
 */
public class ConcurrentMainSearch {

	public static void main(String args[]) {
		String query = "Java";
		Path file = Paths.get("chapter8_data/example1");
		try {
			Date start, end;
			start=new Date();
			List<Product> results = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.parallel()
					.filter(f -> f.toString()
					.endsWith(".txt"))
					.collect(ArrayList<Product>::new,
							//custom accumulater to filter elements
							new ConcurrentObjectAccumulator(query),
							List::addAll);
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
