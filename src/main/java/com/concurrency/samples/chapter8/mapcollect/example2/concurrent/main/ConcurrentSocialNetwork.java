package com.concurrency.samples.chapter8.mapcollect.example2.concurrent.main;

import com.concurrency.samples.chapter8.mapcollect.example2.concurrent.mapper.CommonPersonMapper;
import com.concurrency.samples.chapter8.mapcollect.example2.data.Person;
import com.concurrency.samples.chapter8.mapcollect.example2.data.PersonPair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ConcurrentSocialNetwork {

	public static List<PersonPair> bidirectionalCommonContacts(
			List<Person> people) {

		Map<String, List<PersonPair>> group = people
				.parallelStream()
				.map(new CommonPersonMapper())
				.flatMap(Collection::stream)
				.collect(Collectors.groupingByConcurrent(PersonPair::getFullId));

		//create custom collector. This collector will recive list of strings as input
		Collector<Collection<String>,
				AtomicReference<Collection<String>>, Collection<String>> //--> intermediate data structure
				intersecting = Collector.of(
			    () -> new AtomicReference<>(null), //--> Supplier function. always called when we need to create intermediate data structure
			    (acc, list) -> {  //---> this is accumulator of BiConsumer. (args -> atomic reference , list of string that will be passed)
			        //if no list present, create a new one and store or else add all elements to the list
			        acc.updateAndGet(set -> set == null ? new ConcurrentLinkedQueue<>(list) : set).retainAll(list);
			    },
			    (acc1, acc2) -> { //-> combiner function, only called in parallel streams
			      if (acc1.get() == null)
			        return acc2;
			      if (acc2.get() == null)
			        return acc1;
			      acc1.get().retainAll(acc2.get());
			      return acc1;
			    }, 
			    (acc) -> acc.get() == null ? Collections.emptySet() : acc.get(), //-> finalizer
			    Collector.Characteristics.CONCURRENT, //-> indicates that the accumulator function can be called concurrently
			    Collector.Characteristics.UNORDERED); //--> indicates this will not preserve e the original ordering
		
		
		List<PersonPair> peopleCommonContacts = group.entrySet()
			      .parallelStream()
			      .map((entry) -> {
			        Collection<String> commonContacts =  
			          entry.getValue()
			            .parallelStream()
			            .map(p -> p.getContacts())//generates list of string
			            .collect(intersecting);
			        PersonPair person = new PersonPair();
			        person.setId(entry.getKey().split(",")[0]);
			        person.setOtherId(entry.getKey().split(",")[1]);
			        person.setContacts(new ArrayList<String>(commonContacts));
			        return person;
			      }).collect(Collectors.toList());

		return peopleCommonContacts;

	}

}
