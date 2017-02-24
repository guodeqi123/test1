package org.test.gdq.crawler;

import java.net.SocketTimeoutException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.test.gdq.crawler.bean.BookInfo;
import org.test.gdq.crawler.bean.URLWrapper;

public class PageParser implements Runnable{
	
	private UrlQueue toParse ;
	private UrlQueue toWaste ;
	private ResultData rd ;
	private CrawlerApp app ;
	
	public static String pageBarClass = "paginator";
	public static String bookItemClass = "subject-item";
	public static String nextPageText = "后页";
	public static int pageSize = 20;//页容量默认20
	
	public PageParser( UrlQueue toParse , UrlQueue toWaste ,ResultData rd , CrawlerApp crawlerApp ){
		this.toParse = toParse;
		this.toWaste = toWaste;
		this.rd = rd;
		this.app = crawlerApp;
	}
	
	@Override
	public void run() {
		
		URLWrapper toParseUrl = toParse.deQueueAUrl();
		while( toParseUrl != null && !app.stopFlag){
			String httpUrl = toParseUrl.getHttpUrl();
			System.out.println( "#Queue Remain Size :" + toParse.queueSize()+"#URL TO PARSE IS : " + httpUrl );
			
			try {
				Thread.sleep(CrawlerApp.SLEEPTIME * 1000);//休息5秒 防止被屏蔽
				
				Connection connect = HttpClientUtil.getJsoupConnection(httpUrl, null);
				Document document=connect.get();
				
//				String pageContent = HttpClientUtil.getPageContent(httpUrl);
//				Document document = Jsoup.parse(pageContent);
				
				Element body=document.body();  
				Elements bookItems = body.getElementsByClass(bookItemClass);
				if( bookItems==null || bookItems.size()==0 ){
					continue;
				}
				for( Element tmpEle : bookItems ){
						
					BookInfo bookInfo = parseToABook(tmpEle);
					rd.addABook(bookInfo);
//					System.out.println( "Parse a book : " + bookInfo.getBookName() + " resultDate : " + rd.getResultBooks().size() );
				}
			} catch (Exception e) {
				//大于3次则废弃
				int tryCount = toParseUrl.getTryCount();
				if( tryCount<CrawlerApp.MAX_RETRY_TIME-1 ){
					toParseUrl.setTryCount(tryCount + 1 );
					toParse.enQueueAUrl(toParseUrl);
				}else{
					toWaste.enQueueAUrl(toParseUrl);
				}
				if( e instanceof SocketTimeoutException ){
					System.out.println( "URL :" + httpUrl + " 获取超时!!!!!!!!!!!" );
				}else{
					e.printStackTrace();
				}
			}
			
			toParseUrl = toParse.deQueueAUrl();
		}
		
	}

	private BookInfo parseToABook(Element bookInfoDiv) {
		BookInfo book = new BookInfo();
		
		Elements eles = bookInfoDiv.getElementsByTag("h2");
		Element ele = eles.get(0);
		String bookName = ele.text();
		book.setBookName(bookName);
		
		eles = bookInfoDiv.getElementsByTag("a");
		ele = eles.get(0);
		String bookUrl = ele.attr("href");
		book.setBookUrl(bookUrl);
		
		try {
			eles = bookInfoDiv.getElementsByClass("rating_nums");
			ele = eles.get(0);
			String score = ele.text();
			eles = bookInfoDiv.getElementsByClass("pl");
			ele = eles.get(0);
			String peopleCount = ele.text();
			int peopleCountInt = convert(peopleCount);
			book.setBookScore(Double.parseDouble(score));
			book.setGradePeopleCount(peopleCountInt);
		} catch (Exception e) {
			book.setBookScore( 0 );
			book.setGradePeopleCount( 0 );
			rd.addErrorInfo(book.getBookName()+":评分信息错误!!!");
			return book;
		}
		
		eles = bookInfoDiv.getElementsByClass("pub");
		ele = eles.get(0);
		String publish = ele.text();
		String[] split = publish.split("/");
		if( split.length == 5 ){
			book.setBookAuther( split[0] );
			book.setBookPublishing( split[2] );
			book.setPublicationDate(split[3] );
			book.setBookPrice( split[4] );
		}else if( split.length == 4 ) {
			book.setBookAuther( split[0] );
			book.setBookPublishing( split[1] );
			book.setPublicationDate(split[2] );
			book.setBookPrice( split[3] );
		} else{
			rd.addErrorInfo(book.getBookName()+":" + publish);
		}
		
		return book;
	}

	private int convert(String peopleCount) {
		
		peopleCount=peopleCount.trim();
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<peopleCount.length();i++){
			char charAt = peopleCount.charAt(i);
			if( charAt>=48 && charAt<=57){
				sb.append(charAt);
			}
		}
		
		return Integer.parseInt(sb.toString());
	}

	
}
