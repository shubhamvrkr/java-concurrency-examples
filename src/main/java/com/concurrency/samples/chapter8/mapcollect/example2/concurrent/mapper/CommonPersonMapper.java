package com.concurrency.samples.chapter8.mapcollect.example2.concurrent.mapper;

import com.concurrency.samples.chapter8.mapcollect.example2.data.Person;
import com.concurrency.samples.chapter8.mapcollect.example2.data.PersonPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * This class generates all {@link PersonPair} objects from {@link Person} object;
 * Funtion to transform person instance to list of personpair instance.
 * Used in .map for transformation
 */
public class CommonPersonMapper implements Function<Person, List<PersonPair>> {

	@Override
	public List<PersonPair> apply(Person person) {
		
		List<PersonPair> ret=new ArrayList<>();
		
		List<String> contacts=person.getContacts();
		Collections.sort(contacts);
		
		for (String contact : contacts) {
			PersonPair personExt=new PersonPair();
			if (person.getId().compareTo(contact) < 0) {
				personExt.setId(person.getId());
				personExt.setOtherId(contact);
			} else {
				personExt.setId(contact);
				personExt.setOtherId(person.getId());
			}
			personExt.setContacts(contacts);
			ret.add(personExt);
		}
		
		return ret;
	}

}
