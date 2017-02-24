package org.test.gdq.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Test;

public class TestMain {

//	@Test
	public void testPrint(){
		
		System.out.println( "This is testPrint()" );
		
	}
	
	
	@Test
	public void testPrintFile(){
		
		try {
			String pathname = "export/booklist.csv";
			String sep = ",";
			File file = new File(pathname );
			if( file.exists() ){
				file.delete();
			}
			file.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(file);
			 BufferedWriter bw =new BufferedWriter(new OutputStreamWriter( fos , "GBK")) ;
			 
			String tableTitle = new String( "序号"+sep+"书名" + sep+ "评分" + sep  + "评价人数" +sep + "作者" +sep+  "出版社" + sep +"出版日期" + sep + "价格" );
			
			bw.write( tableTitle );
			
			bw.flush();
			bw.close();
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
