package com.concurrency.samples.chapter6.forkandjoin.example1.serial;

import com.concurrency.samples.chapter6.forkandjoin.example1.common.Word;

public class Document {

	private Word[] data;
	private String name;
	private DocumentCluster cluster;

	public Document(String name, int size) {
		this.name = name;
		data = new Word[size];
		cluster = null;
	}

	public Word[] getData() {
		return data;
	}

	public void setData(Word[] data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DocumentCluster getCluster() {
		return cluster;
	}

	public boolean setCluster(DocumentCluster cluster) {
		if (this.cluster == cluster) {
			return false;
		} else {
			this.cluster = cluster;
			return true;
		}
	}

}
