package com.my.bookstore.core;

import java.util.List;

public interface BookList {

	public Book[] list(String searchString);

	public boolean add(Book book, int quantity);

	public int[] buy(List books);
}
