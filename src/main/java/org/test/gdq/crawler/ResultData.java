package org.test.gdq.crawler;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.test.gdq.crawler.bean.BookInfo;

public class ResultData {
	
	private Set<BookInfo> sortBookInfo = new  TreeSet<BookInfo>(new Comparator<BookInfo>() {
		@Override
		public int compare(BookInfo o1, BookInfo o2) {
			double bookScore = o1.getBookScore();
			double bookScore2 = o2.getBookScore();
			if( bookScore>bookScore2 ){
				return -1;
			}else {
				return 1;
			}
		}
	});
	
	private List<String> cantParsePub = new LinkedList<String>();
	
	public ResultData(){}
	
	
	public synchronized void addABook( BookInfo book){
		
		sortBookInfo.add(book);
		
	}
	
	public synchronized Collection<BookInfo> getResultBooks(){
		return sortBookInfo;
	}
	
	public synchronized List<String> getErrorInfos(){
		return cantParsePub;
	}
	
	public synchronized void addErrorInfo(String info){
		cantParsePub.add(info);
	}
	
}
