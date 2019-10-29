package com.concurrency.samples.chapter8.mapcollect.example1.concurrent.main;

import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;
import com.concurrency.samples.chapter8.mapcollect.example1.data.ProductLoader;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

public class ConcurrentLoaderAccumulator implements
		BiConsumer<List<Product>, Path> {

	@Override
	public void accept(List<Product> list, Path path) {

		Product product= ProductLoader.load(path);
		list.add(product);
		
	}

}
