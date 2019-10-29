package com.concurrency.samples.chapter6.forkandjoin.example1.serial;

import com.concurrency.samples.chapter6.forkandjoin.example1.common.VocabularyLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class SerialMain {

	public static void main(String[] args) throws IOException {

		Path pathVoc = Paths.get("chapter6_data/example1", "movies.words");
		Map<String, Integer> vocIndex = VocabularyLoader.load(pathVoc);
		System.out.println("Voc Size: " + vocIndex.size());

		Path pathDocs = Paths.get("chapter6_data/example1", "movies.data");
		Document[] documents = DocumentLoader.load(pathDocs, vocIndex);
		System.out.println("Document Size: " + documents.length);

		if (args.length != 2) {
			System.err.println("Please specify K and SEED");
			return;
		}
		int K = Integer.valueOf(args[0]);
		int SEED = Integer.valueOf(args[1]);

		Date start, end;
		start = new Date();
		DocumentCluster[] clusters = SerialKMeans.calculate(documents, K, vocIndex.size(), SEED);
		end = new Date();
		System.out.println("K: " + K + "; SEED: " + SEED);
		System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

		System.out.println(
				Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
						.map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));

	}

}
