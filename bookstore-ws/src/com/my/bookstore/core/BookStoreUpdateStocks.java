package com.my.bookstore.core;

public class BookStoreUpdateStocks {
	
	private static BookStoreUpdateStocks updateBookstocks=null;

	private static BookStoreUpdateStocks getInstance(){
		if(updateBookstocks==null){
			updateBookstocks = new BookStoreUpdateStocks();
		}
		return updateBookstocks;
	}
	
	public void updateBookStock(){
		ReadInputUrl readInputUrl = new ReadInputUrl();
		System.out.println(readInputUrl.getBookList());
		//update the RAM cache with books details
		BookStoreCache dc = BookStoreCache.getInstance();
		dc.setDbBooksList(readInputUrl.getBookList());
	}
	
}
