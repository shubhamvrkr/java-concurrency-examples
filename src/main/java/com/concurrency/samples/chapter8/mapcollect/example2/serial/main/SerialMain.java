package com.concurrency.samples.chapter8.mapcollect.example2.serial.main;

import com.concurrency.samples.chapter8.mapcollect.example2.data.DataLoader;
import com.concurrency.samples.chapter8.mapcollect.example2.data.Person;
import com.concurrency.samples.chapter8.mapcollect.example2.data.PersonPair;

import java.util.Date;
import java.util.List;

public class SerialMain {

	public static void main(String[] args) {

		System.out.println("Serial Main Bidirectional - Test");
		Date start, end;
		List<Person> people= DataLoader.load("chapter8_data/example2","test.txt");
		start=new Date();
		List<PersonPair> peopleCommonContacts=SerialSocialNetwork.bidirectionalCommonContacts(people);
		end=new Date();
		peopleCommonContacts.forEach(p -> System.out.println (p.getFullId()+": "+formatContacts(p.getContacts())));
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

		System.out.println("Serial Main Bidirectional - Facebook");
		people=DataLoader.load("chapter8_data/example2","facebook_contacts.txt");
		start=new Date();
		peopleCommonContacts=SerialSocialNetwork.bidirectionalCommonContacts(people);
		end=new Date();
		//peopleCommonContacts.forEach(p -> System.out.println (p.getFullId()+": "+getContacts(p.getContacts())));
		System.out.println("Execution Time: "+(end.getTime()-start.getTime()));

	}

	private static String formatContacts(List<String> contacts) {
		StringBuffer buffer=new StringBuffer();
		for (String contact: contacts) {
			buffer.append(contact+",");
		}
		return buffer.toString();
	}

}
