package com.concurrency.samples.chapter8.mapcollect.example2.concurrent.main;

import com.concurrency.samples.chapter8.mapcollect.example2.data.DataLoader;
import com.concurrency.samples.chapter8.mapcollect.example2.data.Person;
import com.concurrency.samples.chapter8.mapcollect.example2.data.PersonPair;

import java.util.Date;
import java.util.List;

/**
 * Demonstrate the usage of both the version of streams.collect
 * @see ConcurrentSocialNetwork for more details
 */
public class ConcurrentMain {

    public static void main(String[] args) {

        Date start, end;
        System.out.println("Concurrent Main Bidirectional - Test");//load the data
        List<Person> people = DataLoader.load("chapter8_data/example2", "test.txt");
        start = new Date();
        //find person pair
        List<PersonPair> peopleCommonContacts = ConcurrentSocialNetwork.bidirectionalCommonContacts(people);
        end = new Date();
        peopleCommonContacts.forEach(p -> System.out.println(p.getFullId() + ": " + formatContacts(p.getContacts())));
        System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

        System.out.println("Concurrent Main Bidirectional - Facebook");
        people = DataLoader.load("chapter8_data/example2", "facebook_contacts.txt");
        start = new Date();
        //find person pair
        peopleCommonContacts = ConcurrentSocialNetwork.bidirectionalCommonContacts(people);
        end = new Date();
        peopleCommonContacts.forEach(p -> System.out.println (p.getFullId()+": "+formatContacts(p.getContacts())));
        System.out.println("Execution Time: " + (end.getTime() - start.getTime()));

    }

    private static String formatContacts(List<String> contacts) {
        StringBuffer buffer = new StringBuffer();
        for (String contact : contacts) {
            buffer.append(contact + ",");
        }
        return buffer.toString();
    }

}
