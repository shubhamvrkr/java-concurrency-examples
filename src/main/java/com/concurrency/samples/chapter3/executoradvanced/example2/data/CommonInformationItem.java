package com.concurrency.samples.chapter3.executoradvanced.example2.data;

import java.io.StringWriter;
import java.util.Date;

/**
 * This class stores the information of every item of an Rss feed
 * @author author
 *
 */
public class CommonInformationItem {
	
	/**
	 * Title of the item
	 */
	private String title;
	
	/**
	 * Date of the item in text format
	 */
	private String txtDate;
	
	/**
	 * Date of the item as a Date object
	 */
	private Date date;
	
	/**
	 * Link of the item
	 */
	private String link;
	
	/**
	 * Description of the item
	 */
	private StringBuffer description;
	
	/**
	 * Id of the item
	 */
	private String id;
	
	/**
	 * Source (the rss name) of the item
	 */
	private String source;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CommonInformationItem() {
		description=new StringBuffer();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTxtDate() {
		return txtDate;
	}
	
	public void setTxtDate(String txtDate) {
		this.txtDate = txtDate;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getDescription() {
		return description.toString();
	}
	
	public void setDescription(StringBuffer description) {
		this.description = description;
	}

	public void addDescripcion(String txt) {
		description.append(txt);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * Method that converts the Item into the XML format
	 * @return The item converted to an XML String
	 */
	public String toXML() {
		StringWriter writer=new StringWriter();
		
		writer.append("<item>\n");
		writer.append("<ide>\n");
		writer.append(id);
		writer.append("\n</id>\n");
		writer.append("\n<title>\n");
		writer.append(title);
		writer.append("\n</title>\n");
		writer.append("\n<date>\n");
		writer.append(txtDate);
		writer.append("\n</date>\n");
		writer.append("\n<link>\n");
		writer.append(link);
		writer.append("\n</link>\n");
		writer.append("\n<description>\n");
		writer.append(description);
		writer.append("\n</description>\n");
		writer.append("\n</item>\n");

		return writer.toString();
	}
	
	/**
	 * Method that returns the name of the file where store this item
	 * @return The name of the file to store this item
	 */
	public String getFileName() {
		StringWriter writer=new StringWriter();
		writer.append(source);
		writer.append("_");
		writer.append(String.valueOf(description.hashCode()));
		writer.append(".xml");
		
		return writer.toString();
		
	}
}
