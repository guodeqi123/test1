package org.test.gdq.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;



public class GetOnePageTest {
	
	static String pageUrl = "http://book.douban.com/tag/编程?start=0&type=S";
	
	static String pageUrl2 = "https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start=0&type=S";
	
	public static final String gbk_encoding = "GBK";  
    public static final String utf8_encoding = "utf-8";  
    public static final String utf16_encoding = "utf-16";  
    
	//@Test
	public void getOnePageByHttpClient() throws IOException{
		
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
	        HttpHost target = new HttpHost( pageUrl , 80,   "http");  
	        // 依次是代理地址，代理端口号，协议类型  
	        HttpHost proxy = new HttpHost("221.195.40.145",   80 , "http");  
	        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();  
	        // 请求地址  
	        HttpGet httpGet = new HttpGet( pageUrl );  
	        httpGet.setConfig(config);  
			
		    response = closeableHttpClient.execute(target , httpGet);
		    in = response.getEntity().getContent();
		    br = new BufferedReader( new InputStreamReader(  in ) );
		    String line = null;
		    while( (line=br.readLine())!=null ){
		    	System.out.println(line);
		    }
		    br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			br.close();
			in.close();
			response.close();
			closeableHttpClient.close();
		}
            
	}
	
	
	static String pageBarClass = "paginator";
	static String nextPageText = "后页";
	
//	@Test
//	public void getOnePageByJsonp() {
//		try {
//			
//			Document document=Jsoup.connect( pageUrl2 )
//					.userAgent("Mozilla")  
//                    .cookie("auth", "token")  
//                    .timeout(3000)  
//                    .get();  
//			
//			Element body=document.body();  
//			Elements selectElements = body.getElementsByClass( pageBarClass );
//			Element pageBar = selectElements.get(0);
//			Elements pageBtns = pageBar.getElementsByTag("a");
//			
//			for( Element pageBtn : pageBtns ){
//				String text = pageBtn.text();
//				String attr = pageBtn.attr("href");
//				System.out.println(text + ":::" + attr);
//			}
//			
////			Elements bookItems = body.getElementsByClass("subject-item");
////			Element element = bookItems.get(0);
////			Elements bookInfoDivs = element.getElementsByClass("info");
////			Element bookInfoDiv = bookInfoDivs.get(0);
////			Elements eles = bookInfoDiv.getElementsByTag("h2");
////			Element ele = eles.get(0);
////			String bookName = ele.text();
////			System.out.println(bookName);
////			
////			eles = bookInfoDiv.getElementsByTag("a");
////			ele = eles.get(0);
////			String bookUrl = ele.attr("href");
////			System.out.println(bookUrl);
////			
////			eles = bookInfoDiv.getElementsByClass("rating_nums");
////			ele = eles.get(0);
////			String score = ele.text();
////			eles = bookInfoDiv.getElementsByClass("pl");
////			ele = eles.get(0);
////			String peopleCount = ele.text();
////			int peopleCountInt = convert(peopleCount);
////			System.out.println( score +"|||" + peopleCountInt);
////			
////			eles = bookInfoDiv.getElementsByClass("pub");
////			ele = eles.get(0);
////			String publish = ele.text();
////			String[] split = publish.split("/");
////			System.out.println( split[0] +"||"+ split[2] +"||"+split[3] +"||"+ split[4] );
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}

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
