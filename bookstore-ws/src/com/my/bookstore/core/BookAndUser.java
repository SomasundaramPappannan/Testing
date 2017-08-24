package com.my.bookstore.core;

import java.util.List;

public class BookAndUser {

	public BookAndUser(String userName, String email) {
		super();
		this.userName = userName;
		this.email = email;
	}

	private String userName;
	
	private String email;
	
	private List<Book> bookList;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
}
