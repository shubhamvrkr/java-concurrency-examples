package com.concurrency.samples.chapter8.mapcollect.example2.data;


public class PersonPair extends Person {
	
	private String otherId;
	
	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId.intern();
	}
	
	public String getFullId() {
		return getId()+","+otherId;
	}

}
