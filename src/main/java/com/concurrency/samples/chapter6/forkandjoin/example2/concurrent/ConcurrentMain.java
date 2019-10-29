package com.concurrency.samples.chapter6.forkandjoin.example2.concurrent;

import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusData;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusDataLoader;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.FilterData;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Demonstrate cancelling of task in fork join framework
 */
public class ConcurrentMain {

	public static void main(String[] args) {
		Path path = Paths.get("chapter6_data/example2","census-income.data");

		//load the census data
		CensusData data[] = CensusDataLoader.load(path);
		System.out.println("Number of items: " + data.length);

		Date start, end;
		final int SIZE;
		//threashold for divide and conquer
		if (args.length>0) {
			SIZE=Integer.valueOf(args[0]);
		} else {
			SIZE=2000;
		}

		// Test 1: findFirst, exists, in the first ones
        //Data is present at the start of the array
		List<FilterData> filters = new ArrayList<FilterData>();
		FilterData filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);

		start = new Date();
		//find if any and return. needs cancellation of other tasks once data is found
		CensusData result = ConcurrentSearch.findAny(data, filters, SIZE);
		System.out.println("Test 1 - Result: " + result.getReasonForUnemployment());
		end = new Date();
		System.out.println("Test 1 - Execution Time: " + (end.getTime() - start.getTime()));

		// Test 2: findFirst, exists, in the last ones
        //Data is present at the end of the array
		filters = new ArrayList<FilterData>();
		filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("United-States");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Greece");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Private");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("62");
		filters.add(filter);

		start = new Date();
		result = ConcurrentSearch.findAny(data, filters, SIZE);
		System.out.println("Test 2 - Result: " + result.getReasonForUnemployment());
		end = new Date();
		System.out.println("Test 2 - Execution Time: " + (end.getTime() - start.getTime()));

		// Test 3 Doesn't exists
		filters = new ArrayList<FilterData>();
		filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("XXXX");
		filters.add(filter);

		start = new Date();
		result = ConcurrentSearch.findAny(data, filters, SIZE);
		if (result == null) {
			System.out.println("Test 3 - Result: " + result);
		} else {
			System.out.println("Test 3 - Result: "
					+ result.getReasonForUnemployment());
		}
		end = new Date();
		System.out.println("Test 3 - Execution Time: "
				+ (end.getTime() - start.getTime()));

		// Test 4: Error find First
		filters = new ArrayList<FilterData>();
		filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("Dominican-Republic");
		filters.add(filter);

		start = new Date();
		result = ConcurrentSearch.findAny(data, filters, SIZE);
		if (result != null) {
			System.out.println("Test 4 - Results: " + result.getCitizenship());
		}
		end = new Date();
		System.out.println("Test 4 - Execution Time: "
				+ (end.getTime() - start.getTime()));
		
		// Test 5: List all
		filters = new ArrayList<FilterData>();
		filter = new FilterData();
		filter.setIdField(32);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(31);
		filter.setValue("Dominican-Republic");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(1);
		filter.setValue("Not in universe");
		filters.add(filter);
		filter = new FilterData();
		filter.setIdField(14);
		filter.setValue("Not in universe");
		filters.add(filter);

		start = new Date();
		List<CensusData> results = ConcurrentSearch.findAll(data, filters, SIZE);
		System.out.println("Test 5 - Results: " + results.size());
		end = new Date();
		System.out.println("Test 5 - Execution Time: "
				+ (end.getTime() - start.getTime()));

		// Test 6: Error List
		filters = new ArrayList<FilterData>();
		filter = new FilterData();
		filter.setIdField(0);
		filter.setValue("Dominican-Republic");
		filters.add(filter);

		start = new Date();
		results = ConcurrentSearch.findAll(data, filters, SIZE);
		if (results != null) {
			System.out.println("Test 6 - Results: " + results.size());
		}
		end = new Date();
		System.out.println("Test 6 - Execution Time: " + (end.getTime() - start.getTime()));

	}

}
