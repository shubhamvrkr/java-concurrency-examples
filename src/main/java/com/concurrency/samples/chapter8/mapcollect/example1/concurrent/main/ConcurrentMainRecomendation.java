package com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main;

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

/**
 * This demonstrate using default combiners from Collectors class
 */
public class ConcurrentMainRecomendation {

	public static void main(String[] args) {
		String user = "A2JOYUS36FLG4Z";
		Path file = Paths.get("chapter8_data/example1");
		try {
			Date start, end;
			
			start=new Date();
            //get list of products
			List<Product> productList = Files
					.walk(file, FileVisitOption.FOLLOW_LINKS)
					.parallel()
					.filter(f -> f.toString().endsWith(".txt"))
					.collect(ArrayList<Product>::new,
                            //read each file and marshall to product
							new ConcurrentLoaderAccumulator(),
							List::addAll);

			//get products map with buyer as the key
            //this will map userid with the products they had bought
			Map<String, List<ProductReview>> productsByBuyer=productList
					.parallelStream()
					.<ProductReview>flatMap(p -> p.getReviews().stream().map(r -> new ProductReview(p, r.getUser(), r.getValue())))
					.collect(Collectors.groupingByConcurrent( p -> p.getBuyer()));
			
			Map<String,List<ProductReview>> recommendedProducts=
                    //get bought products by our user
                    productsByBuyer.get(user)
					.parallelStream()
					.map(p -> p.getReviews())//get reviews
					.flatMap(Collection::stream)    //convert list of reviews to each review
					.map(r -> r.getUser()) //for each review get user
					.distinct()  //find distinct user for the
					.map(productsByBuyer::get)//get products bought by the other user
					.flatMap(Collection::stream)//convert list of prouct review to each item
					.collect(Collectors.groupingByConcurrent(p -> p.getTitle())); //filter title from the product
			
            //sort recommended products based on user ratings
			List<ProductRecommendation> recommendations = recommendedProducts
					.entrySet()
					.parallelStream()
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
