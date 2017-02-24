package org.test.gdq.crawler;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.test.gdq.crawler.bean.URLWrapper;

/**
 * 
 * @author Guodeqi
 *
 */
public class UrlQueue {
	
	private LinkedHashSet<URLWrapper> urls = new LinkedHashSet<URLWrapper>();
			
	public UrlQueue(){}		

	
	public synchronized void enQueueAUrl( URLWrapper url ){
		urls.add(url);
	}
	
	public synchronized URLWrapper deQueueAUrl(  ){
		Iterator<URLWrapper> iterator = urls.iterator();
		if( iterator.hasNext() ){
			URLWrapper next = iterator.next();
			urls.remove(next);
			return next;
		}
		return null;
	}
	
	public synchronized Integer queueSize(  ){
		return urls.size();
	}
	
}
