package com.concurrency.samples.chapter8.mapcollect.example1.data;

public class ProductRecommendation implements Comparable<ProductRecommendation>{
	
	private String title;
	private double value;
	
	public ProductRecommendation(String title, double value) {
		this.title=title;
		this.value=value;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int compareTo(ProductRecommendation o) {
		if (this.getValue() > o.getValue()) { 
			return -1;
		}
		
		if (this.getValue() < o.getValue()) {
			return 1;
		}
		return 0;
	}
	
	
	

}
