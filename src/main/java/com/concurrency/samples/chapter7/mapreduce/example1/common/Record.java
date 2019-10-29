package com.concurrency.samples.chapter7.mapreduce.example1.common;

public class Record {
	private int age;
	private String job;
	private String marital;
	private String education;
	private boolean defaultCredit;
	private boolean housing;
	private boolean loan;
	private String contact;
	private String month;
	private String dayOfWeek;
	private int duration;
	private int campaign;
	private int pdays;
	private int previous;
	private int poutcome;
	private double empVarRate;
	private double consPriceIdx;
	private double consConfIdx;
	private double euribor3m;
	private double nrEmployed;
	private String subscribe;

	public Record() {

	}

	/**
	 * @param t
	 */
	public Record(String[] t) {
		age = Integer.valueOf(t[0]);
		job = t[1].replace("\"", "");
		marital = t[2].replace("\"", "");
		education = t[3].replace("\"", "");
		defaultCredit = t[4].replace("\"", "").equals("yes");
		housing = t[5].replace("\"", "").equals("yes");
		loan = t[6].replace("\"", "").equals("yes");
		contact = t[7].replace("\"", "");
		month = t[8].replace("\"", "");
		dayOfWeek = t[9].replace("\"", "");
		duration = Integer.valueOf(t[10]);
		campaign = Integer.valueOf(t[11]);
		pdays = Integer.valueOf(t[12]);
		previous = Integer.valueOf(t[13]);
		if (t[14].startsWith("\"")) {
			poutcome = 0;
		} else {
			poutcome = Integer.valueOf(t[14]);
		}
		empVarRate = Double.valueOf(t[15]);
		consPriceIdx = Double.valueOf(t[16]);
		consConfIdx = Double.valueOf(t[17]);
		euribor3m = Double.valueOf(t[18]);
		nrEmployed = Double.valueOf(t[19]);
		subscribe = t[20].replace("\"", "");
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getMarital() {
		return marital;
	}

	public void setMarital(String marital) {
		this.marital = marital;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public boolean isDefaultCredit() {
		return defaultCredit;
	}

	public void setDefaultCredit(boolean defaultCredit) {
		this.defaultCredit = defaultCredit;
	}

	public boolean isHousing() {
		return housing;
	}

	public void setHousing(boolean housing) {
		this.housing = housing;
	}

	public boolean isLoan() {
		return loan;
	}

	public void setLoan(boolean loan) {
		this.loan = loan;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCampaign() {
		return campaign;
	}

	public void setCampaign(int campaign) {
		this.campaign = campaign;
	}

	public int getPdays() {
		return pdays;
	}

	public void setPdays(int pdays) {
		this.pdays = pdays;
	}

	public int getPrevious() {
		return previous;
	}

	public void setPrevious(int previous) {
		this.previous = previous;
	}

	public int getPoutcome() {
		return poutcome;
	}

	public void setPoutcome(int poutcome) {
		this.poutcome = poutcome;
	}

	public double getEmpVarRate() {
		return empVarRate;
	}

	public void setEmpVarRate(double empVarRate) {
		this.empVarRate = empVarRate;
	}

	public double getConsPriceIdx() {
		return consPriceIdx;
	}

	public void setConsPriceIdx(double consPriceIdx) {
		this.consPriceIdx = consPriceIdx;
	}

	public double getConsConfIdx() {
		return consConfIdx;
	}

	public void setConsConfIdx(double consConfIdx) {
		this.consConfIdx = consConfIdx;
	}

	public double getEuribor3m() {
		return euribor3m;
	}

	public void setEuribor3m(double euribor3m) {
		this.euribor3m = euribor3m;
	}

	public double getNrEmployed() {
		return nrEmployed;
	}

	public void setNrEmployed(double nrEmployed) {
		this.nrEmployed = nrEmployed;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	
	public boolean isNotSubscriber() {
		return this.subscribe.equals("no");
	}

}
