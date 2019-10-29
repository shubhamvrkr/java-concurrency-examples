package com.concurrency.samples.chapter8.mapcollect.example1.serial.main;

import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;
import com.concurrency.samples.chapter8.mapcollect.example1.data.ProductLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * Custom accumulator for collect funtion to transforn the stream element to intermediate result
 */
public class SerialObjectAccumulator implements BiConsumer<ArrayList<Product>, Path> {

	private String word;

	public SerialObjectAccumulator(String word) {
		this.word = word;
	}

	//filters out the product which contains search key word in products title
	@Override
	public void accept(ArrayList<Product> list, Path path) {

		Product product= ProductLoader.load(path);
		
		if (product.getTitle().toLowerCase().contains(word.toLowerCase())) {
			list.add(product);
		}
		
	}

}
