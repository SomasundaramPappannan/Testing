package com.my.bookstore.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ReadInputUrl {
	
	private List bookList = new ArrayList();

	public ReadInputUrl(){
		try {
			
			//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-wsa.esl.cisco.com", 80));
			URL url = new URL(
					"https://raw.githubusercontent.com/contribe/contribe/dev/bookstoredata/bookstoredata.txt");
			URLConnection urlConnect = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));

			String inputLine;
			int bookId=1;
			while ((inputLine = in.readLine()) != null){
				Book book = processRawText(inputLine,bookId);
				System.out.println(bookId);
				bookList.add(book);
				bookId++;
			}
				 
			in.close();

		} catch (Exception e) {
			exceptionWhileFetchingData();
			e.printStackTrace();
		}  
	}
	
	public List getBookList() {
		return bookList;
	}

	private Book processRawText(String oneLine,int bookId){
		String[] bookDetailsArray = oneLine.split(";");
		System.out.println(bookDetailsArray);
		Book book = new Book();
		book.setTitle(bookDetailsArray[0]);
		book.setAuthor(bookDetailsArray[1]);
		book.setPrice(bookDetailsArray[2]);
		book.setAvailableStock(Integer.valueOf(bookDetailsArray[3]));
		book.setBookId(bookId);
		return book;
	}
	
	private void exceptionWhileFetchingData(){
		//Do this for all books
		Book book = new Book();
		book.setTitle("Mastering едц");
		book.setAvailableStock(15);
		book.setBookId(1);
		book.setPrice("762.00");
		book.setAuthor("Average Swede");
		bookList.add(book);
		
		  book = new Book();
		book.setTitle("How To Spend Money");
		book.setAvailableStock(1);
		book.setBookId(2);
		book.setPrice("1000000.00");
		book.setAuthor("Rich Bloke");
		bookList.add(book);
		
		book = new Book();
		book.setTitle("Generic Title");
		book.setAvailableStock(5);
		book.setBookId(3);
		book.setPrice("185.50");
		book.setAuthor("First Author");
		bookList.add(book);
		
		book = new Book();
		book.setTitle("Generic Title");
		book.setAvailableStock(3);
		book.setBookId(4);
		book.setPrice("1748.50");
		book.setAuthor("Second Author");
		bookList.add(book);
		
		book = new Book();
		book.setTitle("Random Sales");
		book.setAvailableStock(20);
		book.setBookId(5);
		book.setPrice("999.00");
		book.setAuthor("Cunning Bastard");
		bookList.add(book);
		
		book = new Book();
		book.setTitle("Random Sales");
		book.setAvailableStock(3);
		book.setBookId(6);
		book.setPrice("499.50");
		book.setAuthor("Cunning Bastard");
		bookList.add(book);
		
		book = new Book();
		book.setTitle("Desired");
		book.setAvailableStock(3);
		book.setBookId(7);
		book.setPrice("564.50");
		book.setAuthor("Rich Bloke");
		bookList.add(book);
	}
	
}
