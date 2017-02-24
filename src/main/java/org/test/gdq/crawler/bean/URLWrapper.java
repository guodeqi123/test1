package org.test.gdq.crawler.bean;

public class URLWrapper {
	
	private String httpUrl = "";
	
	private int tryCount = 0;
	
	public URLWrapper( String httpUrl ){
		this.httpUrl = httpUrl;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if( obj==null ){
			return false;
		}
		if( this==obj ){
			return true;
		}
		if( !(obj instanceof URLWrapper) ){
			return false;
		}
		URLWrapper uw = (URLWrapper)obj;
		if( uw.getHttpUrl().equals(this.httpUrl) ){
			return true;
		}
		
		return super.equals(obj);
	}
}
