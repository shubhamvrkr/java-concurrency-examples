package com.concurrency.samples.chapter4.callableandfutures.example2.common;

import java.util.Map;

public class Document {
	
	private String fileName;
	private Map <String, Integer> voc;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<String, Integer> getVoc() {
		return voc;
	}
	public void setVoc(Map<String, Integer> voc) {
		this.voc = voc;
	}
	
	
}
