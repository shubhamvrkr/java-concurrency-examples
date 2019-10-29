package com.concurrency.samples.chapter6.forkandjoin.example2.serial;

import com.concurrency.samples.chapter6.forkandjoin.example2.common.CensusData;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.Filter;
import com.concurrency.samples.chapter6.forkandjoin.example2.common.FilterData;

import java.util.ArrayList;
import java.util.List;

public class SerialSearch {
	
	public static CensusData findAny (CensusData[] data, List<FilterData> filters) {

		int index=0;
		for (CensusData censusData : data) {
			if (Filter.filter(censusData, filters)) {
				System.out.println("Found: "+index);
				return censusData;
			}
			index++;
		}
		
		return null;
	}
	
	public static List<CensusData> findAll (CensusData[] data,  List<FilterData> filters) {
		List<CensusData> results=new ArrayList<CensusData>();
		
		for (CensusData censusData : data) {
			if (Filter.filter(censusData, filters)) {
				results.add(censusData);
			}
		}
		
		return results;
	}

}
