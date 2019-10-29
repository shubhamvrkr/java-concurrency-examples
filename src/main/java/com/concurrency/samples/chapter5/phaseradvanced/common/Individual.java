package com.concurrency.samples.chapter5.phaseradvanced.common;

//Stores possible solution to the problem
public class Individual implements Comparable<Individual> {

    //order of the cities that needs to be travelled
	private Integer[] chromosomes;
	
	private int value;

	
	public Individual(int size) {
		chromosomes=new Integer[size];
	}
	
	public Individual(Individual other) {
		chromosomes = other.getChromosomes().clone();
	}
	
	@Override 
	public int compareTo(Individual o) {
		  return Integer.compare(this.getValue(), o.getValue());
	}


	public Integer[] getChromosomes() {
		return chromosomes;
	}


	public void setChromosomes(Integer[] chromosomes) {
		this.chromosomes = chromosomes;
	}


	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String ret="";
		for (Integer number: chromosomes) {
			ret+=number+";";
		}
		return ret;
	}
	
	
	

}
