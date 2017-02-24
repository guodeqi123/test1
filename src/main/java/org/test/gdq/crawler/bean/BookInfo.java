package org.test.gdq.crawler.bean;

public class BookInfo {
	
	private Integer id ; 
	private String bookName ; 
	private double bookScore ; 
	private Integer gradePeopleCount ; 
	private String bookAuther ; 
	private String bookPublishing ; 	//出版社
	private String publicationDate ; 	//出版日期
	private String bookPrice ; 	//价格
	
	private String bookUrl = "";
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public double getBookScore() {
		return bookScore;
	}
	public void setBookScore(double bookScore) {
		this.bookScore = bookScore;
	}
	public Integer getGradePeopleCount() {
		return gradePeopleCount;
	}
	public void setGradePeopleCount(Integer gradePeopleCount) {
		this.gradePeopleCount = gradePeopleCount;
	}
	public String getBookAuther() {
		return bookAuther;
	}
	public void setBookAuther(String bookAuther) {
		this.bookAuther = bookAuther;
	}

	public String getBookPublishing() {
		return bookPublishing;
	}
	public void setBookPublishing(String bookPublishing) {
		this.bookPublishing = bookPublishing;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(String bookPrice) {
		this.bookPrice = bookPrice;
	}
	public String getBookUrl() {
		return bookUrl;
	}
	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}
	
 
	@Override
	public String toString() {
		String sep = ",";
		return id + sep  + bookName + sep + bookScore
				+ sep + gradePeopleCount + sep + bookAuther + sep + bookPublishing
				+ sep + publicationDate + sep + bookPrice ;
	}
	
}
