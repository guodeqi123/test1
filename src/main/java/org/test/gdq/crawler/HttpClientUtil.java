package org.test.gdq.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class HttpClientUtil {
	
	private static String proxyIp = "101.68.63.217";
	private static int proxyPort = 8998;
	
	
	public static String getPageContent( String httpUrl ) throws IOException{
		
		StringBuilder sb = new StringBuilder();
		CloseableHttpResponse response = null;
		CloseableHttpClient closeableHttpClient = null;
		InputStream in = null;
		BufferedReader br = null;
		 try {
			 // 创建HttpClientBuilder  
	        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	        // HttpClient  
	        closeableHttpClient = httpClientBuilder.build();  
	        // 依次是目标请求地址，端口号,协议类型  
	        HttpHost target = new HttpHost( httpUrl , 80,   "http");  
	        // 依次是代理地址，代理端口号，协议类型  
	        HttpHost proxy = new HttpHost( proxyIp , proxyPort , "http");  
	        int timeout = 10*1000;
	        RequestConfig config = RequestConfig.custom().setProxy(proxy).
	        		setConnectTimeout( timeout ).setConnectionRequestTimeout( timeout ).build();  
	        // 请求地址  
	        HttpGet httpGet = new HttpGet( httpUrl );  
	        httpGet.setConfig(config);  
			
		    response = closeableHttpClient.execute(target , httpGet);
		    in = response.getEntity().getContent();
		    br = new BufferedReader( new InputStreamReader(  in ) );
		    String line = null;
		    while( (line=br.readLine())!=null ){
		    	sb.append(line);
		    }
		    br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			closeableHttpClient.close();
			response.close();
			br.close();
			in.close();
		}
		 return sb.toString();
	}
	
	public static Connection getJsoupConnection( String url , Integer timeout){
		
		if( timeout == null ){
			timeout = 10000;
		}
		Connection connect = Jsoup.connect( url ).timeout(timeout);
		connect.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		
		return connect;
	}
	
}
