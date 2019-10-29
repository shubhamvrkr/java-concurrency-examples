package com.concurrency.samples.chapter8.mapcollect.example1.serial.main;

import com.concurrency.samples.chapter8.mapcollect.example1.data.Product;
import com.concurrency.samples.chapter8.mapcollect.example1.data.ProductLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BiConsumer;

//Implementation of custom BiConsumer accumulater to be used in collect terminary operator
public class SerialLoaderAccumulator implements BiConsumer<ArrayList<Product>, Path> {

	@Override
	public void accept(ArrayList<Product> list, Path path) {

		Product product= ProductLoader.load(path);
		list.add(product);
		
	}

}
