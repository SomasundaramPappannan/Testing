package com.my.bookstore.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookStoreCache {

	private static List dbBooksList = new ArrayList();
	
 	
	private static BookStoreCache database=null;
	
	public static BookStoreCache getInstance(){
		if(database==null){
			database = new BookStoreCache();
		}
		return database;
	}

	public  List getDbBooksList() {
		return dbBooksList;
	}

	public  void setDbBooksList(List dbBooksList) {
		BookStoreCache.dbBooksList = dbBooksList;
	}

 
	 
}
