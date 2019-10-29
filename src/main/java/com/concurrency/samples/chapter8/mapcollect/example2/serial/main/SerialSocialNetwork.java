package com.concurrency.samples.chapter8.mapcollect.example2.serial.main;

import com.concurrency.samples.chapter8.mapcollect.example2.concurrent.mapper.CommonPersonMapper;
import com.concurrency.samples.chapter8.mapcollect.example2.data.Person;
import com.concurrency.samples.chapter8.mapcollect.example2.data.PersonPair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SerialSocialNetwork {

	public static List<PersonPair> bidirectionalCommonContacts(
			List<Person> people) {

		Map<String, List<PersonPair>> group = people
				.stream()
				.map(new CommonPersonMapper())
				.flatMap(Collection::stream)
				.collect(Collectors.groupingBy(PersonPair::getFullId));

		Collector<Collection<String>, Collection<String>, Collection<String>> intersecting = Collector.of(
			    () -> new ArrayList<String>(), 
			    (acc, list) -> {
			    	if (acc.isEmpty()) {
			    		acc.addAll(list);
			    	} else {
			    		acc.retainAll(list);
			    	}
			    },
			    (acc1, acc2) -> {
			      if (acc1.isEmpty())
			        return acc2;
			      if (acc2.isEmpty())
			        return acc1;
			      acc1.retainAll(acc2);
			      return acc1;
			    }, 
			    (acc) -> acc);
		
		
		List<PersonPair> peopleCommonContacts = group.entrySet()
			      .stream()
			      .map((entry) -> {
			        Collection<String> commonContacts =  
			          entry.getValue()
			            .stream()
			            .map(p -> p.getContacts())
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
