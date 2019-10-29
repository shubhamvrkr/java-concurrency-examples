package com.concurrency.samples.chapter8.mapcollect.example1.serial.main;

import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;
import com.concurrency.samples.chapter8.mapcollect.example1.data.ProductRecommendation;
import com.concurrency.samples.chapter8.mapcollect.example1.data.ProductReview;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SerialMainRecomendation {

	public static void main(String[] args) {
		String user = "A2JOYUS36FLG4Z";
		Path file = Paths.get("chapter8_data/example1");
		try {
			Date start, end;
			
			start=new Date();
			//get list of products from files
			List<Product> productList = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.filter(f -> f.toString().endsWith(".txt"))
					.collect(ArrayList<Product>::new,
							new SerialLoaderAccumulator(),
							List::addAll);
			
			Map<String, List<ProductReview>> productsByBuyer=productList
					.stream()
					.<ProductReview>flatMap(p -> p.getReviews().stream().map(r -> new ProductReview(p, r.getUser(), r.getValue())))
					.collect(Collectors.groupingBy( p -> p.getBuyer()));
			
			Map<String,List<ProductReview>> recommendedProducts=productsByBuyer.get(user)
					.stream()
					.map(p -> p.getReviews())
					.flatMap(Collection::stream)
					.map(r -> r.getUser())
					.distinct()
					.map(productsByBuyer::get)
					.flatMap(Collection::stream)
					.collect(Collectors.groupingBy(p -> p.getTitle()));
			

			List<ProductRecommendation> recommendations = recommendedProducts
					.entrySet()
					.stream()
					.map(entry -> new ProductRecommendation(
							entry.getKey(), 
							entry.getValue().stream().mapToInt(p -> p.getValue()).average().getAsDouble()))
					.sorted()
					.collect(Collectors.toList());
			
			end=new Date();
			
			recommendations. forEach(pr -> System.out.println(pr.getTitle()+": "+pr.getValue()));

			
			System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

}
