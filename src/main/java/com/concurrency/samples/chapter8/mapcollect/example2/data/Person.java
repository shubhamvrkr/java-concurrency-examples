package com.concurrency.samples.chapter8.mapcollect.example2.data;

import java.util.ArrayList;
import java.util.List;

public class Person {
	
	private String id;
	private List<String> contacts;
	
	public Person() {
		contacts=new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id.intern();
	}
	
	public List<String> getContacts() {
		return contacts;
	}
	
	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}
	
	public void addContact(String contact) {
		contacts.add(contact);
	}
}
