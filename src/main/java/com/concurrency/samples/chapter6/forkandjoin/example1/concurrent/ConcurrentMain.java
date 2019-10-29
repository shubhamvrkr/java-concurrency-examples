package com.concurrency.samples.chapter6.forkandjoin.example1.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example1.common.VocabularyLoader;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.Document;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.DocumentCluster;
import com.concurrency.samples.chapter6.forkandjoin.example1.serial.DocumentLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstrate fork join framework using RecursiveAction that does not return data
 * Other important classes are
 * RecursiveTask - returns result
 * CounterCompleter - used to trigger other task
 *
 */
public class ConcurrentMain {

	public static void main(String[] args) throws IOException {
		
		Path pathVoc = Paths.get("chapter6_data/example1\\movies.words");
		//load words mapped with index
		Map<String, Integer> vocIndex= VocabularyLoader.load(pathVoc);
		System.out.println("Voc Size: "+vocIndex.size());
		
		Path pathDocs = Paths.get("chapter6_data/example1\\movies.data");
		//load documents
		Document[] documents= DocumentLoader.load(pathDocs, vocIndex);
		System.out.println("Document Size: "+documents.length);
		
		if (args.length != 3) {
			System.err.println("Please specify K, SEED, MIN_SIZE");
			return;
		}
		int K = Integer.valueOf(args[0]);
		int SEED = Integer.valueOf(args[1]);
		int MAX_SIZE = Integer.valueOf(args[2]);
	
		
		Date start, end;
		start = new Date();
		DocumentCluster[] clusters = ConcurrentKMeans.calculate(documents, K, vocIndex.size(), SEED, MAX_SIZE );
		end = new Date();
		System.out.println("K: " + K + "; SEED: " + SEED+"; MAX_SIZE: "+MAX_SIZE);
		System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

		System.out.println(
				Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
						.map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));

	}

}
