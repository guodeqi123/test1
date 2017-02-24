package org.test.gdq.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.test.gdq.crawler.bean.BookInfo;
import org.test.gdq.crawler.bean.URLWrapper;

/**
 * 
 * @author Guodeqi
 *
 */
public class CrawlerApp {
	
	public static int MAX_THREAD_COUNT = 10;//线程数量 x
	public static int MAX_RETRY_TIME = 1;//一个URL 直解析1次
	public static int SLEEPTIME = 5;//间隔5S
	public static String firstPageUrl = "https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start=0&type=S"; //同一URL解析3此仍出错 则废弃
	
	private UrlQueue toParseUrls;
	private UrlQueue wasteUrls ;
	private ResultData rd ;
	private List<Thread> runThread = new ArrayList<Thread>();
	public boolean stopFlag = false;
	
	public CrawlerApp(){
		//创建对象
		toParseUrls = new UrlQueue();
		wasteUrls = new UrlQueue();
		rd = new ResultData();
		
	}
	
	public void initCrawler() throws IOException{
		
		//1获取第一页中的所有页码 来计算编程类的书有多少URL
//		String pageContent = HttpClientUtil.getPageContent(firstPageUrl);
//		Document document = Jsoup.parse(pageContent);
		
		Connection connect = HttpClientUtil.getJsoupConnection(firstPageUrl, null);
		Document document=connect.get();
		
		Element body=document.body();  
		Elements selectElements = body.getElementsByClass( PageParser.pageBarClass );
		Element pageBar = selectElements.get(0);
		Elements pageBtns = pageBar.getElementsByTag("a");
		int size = pageBtns.size();
		Element element = pageBtns.get(size-2);
		String maxPageNum = element.text();
		int parseInt = Integer.parseInt(maxPageNum.trim());
		
		String urlPrefix = "https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start="; 
		String urlSuffix = "&type=S"; 
		for( int i=0; i<parseInt ;i++ ){
			int startIndex = 20*i;
			String tmpUrl = urlPrefix + startIndex + urlSuffix;
			URLWrapper urlWrapper = new URLWrapper(tmpUrl);
			toParseUrls.enQueueAUrl(urlWrapper);
		}
		double aaa = (double)parseInt/ (double)MAX_THREAD_COUNT *SLEEPTIME; 
		System.out.println( "Max page Num is : " + maxPageNum  + " URL size is : " + toParseUrls.queueSize() + " Predict coast time :" +aaa +"S");
	}
	
	public void startCrawler() throws InterruptedException{
		//启动线程进行解析
		for(  int i=0; i<MAX_THREAD_COUNT ;i++ ) {
			Thread.sleep( SLEEPTIME * 1000 );
			PageParser pageParserRun = new PageParser(toParseUrls, wasteUrls, rd , this);
			Thread thread = new Thread( pageParserRun , "PageParser-"+i  );
			thread.start();
			runThread.add(thread);
		}
	}
	
	public void printResult() throws Exception{
		
		FileOutputStream fos = null; 
		BufferedWriter bw = null;
		
		try {
			String pathname = "export/booklist.csv";
			String sep = ",";
			File file = new File(pathname );
			if( file.exists() ){
				file.delete();
			}
			file.createNewFile();
			
			fos = new FileOutputStream(file);
			bw =new BufferedWriter(new OutputStreamWriter( fos , "GBK")) ;
			 
			String tableTitle = new String( "序号"+sep+"书名" + sep+ "评分" + sep  + "评价人数" +sep + "作者" +sep+  "出版社" + sep +"出版日期" + sep + "价格" );
			
			bw.write( tableTitle );
			bw.newLine();
			
			Collection<BookInfo> resultBooks = rd.getResultBooks();
			int saveRows = 40;
			int i=0;
			Iterator<BookInfo> iterator = resultBooks.iterator();
			while( iterator.hasNext() && i<saveRows ){
				BookInfo next = iterator.next();
				//若评价人数少于1000则过滤掉该记录
				if( next.getGradePeopleCount()<1000 ){
					continue;
				}
				i++;
				next.setId(i);
				bw.write( next.toString() );
				bw.newLine();
			}
			bw.flush();
		} catch (Exception e) {
			throw e;
		}finally{
			bw.close();
			fos.close();
		}
		
		
		List<String> errorInfos = rd.getErrorInfos();
		for( String str : errorInfos  ){
			System.out.println(   "xxxxxxxxxxxxxx" + str );
		}
		
	}
	
	
	public static void main(String[] args) {
		
		try {
			long startTime = System.currentTimeMillis();
			
			CrawlerApp app = new CrawlerApp();
			app.initCrawler();
			app.startCrawler();
			for(  Thread ttt : app.runThread ){
				ttt.join();
			}
			app.printResult();
			
			long endTime = System.currentTimeMillis();
			System.out.println("---coast time :"+ (endTime-startTime)+"ms . and book totle size is :" + app.rd.getResultBooks().size() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
