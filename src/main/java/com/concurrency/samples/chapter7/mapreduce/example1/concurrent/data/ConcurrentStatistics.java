package com.concurrency.samples.chapter7.mapreduce.example1.concurrent.data;

import com.concurrency.samples.chapter7.mapreduce.example1.common.Record;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConcurrentStatistics {

	public static void jobDataFromSubscribers(List<Record> records) {
		System.out.println("****************************************");
		System.out.println("Job info for Deposit suscribers");

		ConcurrentMap<String, List<Record>> map = records.parallelStream()
				.filter(r -> r.getSubscribe().equals("yes"))
				.collect(Collectors.groupingByConcurrent(Record::getJob));

		map.forEach((k, l) -> System.out.println(k + ": " + l.size()));

		System.out.println("****************************************");
	}

	public static void ageDataFromSubscribers(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("Age info for Deposit suscribers");

		
				
		DoubleSummaryStatistics statistics = records.parallelStream()
				.filter(r -> r.getSubscribe().equals("yes"))
				.collect(Collectors.summarizingDouble(Record::getAge));

		System.out.println("Min: " + statistics.getMin());
		System.out.println("Max: " + statistics.getMax());
		System.out.println("Average: " + statistics.getAverage());
		System.out.println("****************************************");
	}

	public static void maritalDataFromSubscribers(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("Marital info for Deposit suscribers");

		records.parallelStream()
				.filter(r -> r.getSubscribe().equals("yes"))
				.map(r -> r.getMarital())
				.distinct()
				.sorted()
				.forEachOrdered(System.out::println);
		System.out.println("****************************************");
	}

	public static void campaignDataFromNonSubscribersBad(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("Number of contacts for Non Suscriber");

		IntStream stream = records.parallelStream()
				.filter(Record::isNotSubscriber)
				.mapToInt(r -> r.getCampaign());

		System.out
				.println("Max number of contacts: " + stream.max().getAsInt());
		System.out
				.println("Min number of contacts: " + stream.min().getAsInt());
		System.out.println("****************************************");
	}

	public static void campaignDataFromNonSubscribersOk(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("Number of contacts for Non Suscriber");
		int value = records.parallelStream()
				.filter(Record::isNotSubscriber)
				.map(r -> r.getCampaign())
				.mapToInt(Integer::intValue)
				.max()
				.getAsInt();

		System.out.println("Max number of contacts: " + value);

		value = records.parallelStream()
				.filter(Record::isNotSubscriber)
				.map(r -> r.getCampaign())
				.mapToInt(Integer::intValue)
				.min()
				.getAsInt();

		System.out.println("Min number of contacts: " + value);
		System.out.println("****************************************");
	}

	public static void multipleFilterData(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("Multiple filter");

		Stream<Record> stream1 = records.parallelStream()
				.filter(Record::isDefaultCredit);
		Stream<Record> stream2 = records.parallelStream()
				.filter(r -> !(r.isHousing()));
		Stream<Record> stream3 = records.parallelStream()
				.filter(r -> !(r.isLoan()));

		Stream<Record> complete = Stream.concat(stream1, stream2);
		complete = Stream.concat(complete, stream3);

		long value = complete.parallel().unordered().distinct().count();

		System.out.println("Number of people: " + value);
		System.out.println("****************************************");
	}

	public static void multipleFilterDataPredicate (List<Record> records) {
		
		System.out.println("****************************************");
		System.out.println("Multiple filter with Predicate");

		Predicate<Record> p1 = r -> r.isDefaultCredit();
		Predicate<Record> p2 = r -> !r.isHousing();
		Predicate<Record> p3 = r -> !r.isLoan();

		Predicate<Record> pred = Stream.of(p1, p2, p3).reduce(Predicate::or).get();

		long value = records.parallelStream().filter(pred).count();

		System.out.println("Number of people: " + value);
		System.out.println("****************************************");
	}
	
	public static void durationDataForNonSuscribers(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("Duration data for non suscribers");
		records.parallelStream()
				.filter(r -> r.isNotSubscriber())
				.sorted(Comparator.comparingInt(Record::getDuration).reversed())
				.limit(10)
				.forEachOrdered(
						r -> System.out.println("Education: "
								+ r.getEducation() + "; Duration: "
								+ r.getDuration()));
		System.out.println("****************************************");
	}
	
	public static void peopleBetween25and50(List<Record> records) {

		System.out.println("****************************************");
		System.out.println("People between 25 and 50");
		int count=records.parallelStream()
			.map(r -> r.getAge())
			.filter(a -> (a >=25 ) && (a <=50))
			.mapToInt(a -> 1)
			.reduce(0, Integer::sum);
		System.out.println("People between 25 and 50: "+count);
		System.out.println("****************************************");
	}
	

}
