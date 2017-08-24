package com.my.bookstore.core.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.my.bookstore.core.Book;
import com.my.bookstore.core.BookStoreCache;

@Path("/bookservice")
public class BookStoreRestService {

	
	@POST
	@Path("/login/{username}/{emailid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response storeUserDetailsInsession(@PathParam("username")String username,@PathParam("emailid")String emailid,@Context HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("USERNAME", username);
		session.setAttribute("EMAILID", emailid);
		return Response.ok("Login successfull").build();
	}

	@POST
	@Path("/getbook/{bookid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getbook(@PathParam("bookid")String bookid){
		BookStoreCache  cache =  BookStoreCache.getInstance();
		int iBook = Integer.parseInt(bookid);
		Iterator itr = cache.getDbBooksList().iterator();
		Book bookincache = null;
		while(itr.hasNext()){
			bookincache = (Book)itr.next();
			if(bookincache.getBookId()==iBook){
				break;
			}
		}
		
		return Response.ok(bookincache).build();
	}
	
	
	@POST
	@Path("/search/{searchTerm}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("searchTerm") String searchTerm){
		List searchResultList = new ArrayList();
		BookStoreCache  cache =  BookStoreCache.getInstance();
		Iterator itr = cache.getDbBooksList().iterator();
		System.out.println(searchResultList.size());
		while(itr.hasNext()){
			Book book = (Book)itr.next();
			if(book.getTitle().contains(searchTerm)){
				searchResultList.add(book);
				System.out.println("Adding ..."+book);
			}
		}
		return Response.ok(searchResultList).build() ;
		
	}
	
	@GET
	@Path("/displayall")
	@Produces(MediaType.APPLICATION_JSON)
	public Response displayAll(){
		BookStoreCache  cache =  BookStoreCache.getInstance();
		return Response.ok(cache.getDbBooksList()).build() ;
	}

	@POST
	@Path("/addbooktocart/{bookid}/{quantity}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response add(@PathParam("bookid") String bookId,@PathParam("quantity") String quantity, @Context HttpServletRequest request){
		BookStoreCache  cache =  BookStoreCache.getInstance();
		Iterator itr = cache.getDbBooksList().iterator();
		Book book = null;
 		while(itr.hasNext()){
			book = (Book)itr.next();
			if(book.getBookId()==Integer.valueOf(bookId)){
				 break;
			}
		}
 		book.setAvailableStock(Integer.valueOf(quantity));
 		double price = Double.valueOf(book.getPrice());
 		price = price * Integer.valueOf(quantity);
 		book.setPrice(String.valueOf(price));
 		System.out.println("Adding to cart..."+book);
 		HttpSession session = request.getSession();
 		List cartList = null;
 		if(session.getAttribute("CART")!=null){
 			 cartList = (List)session.getAttribute("CART");
 			cartList.add(book);
 		}else{
 			 cartList = new ArrayList();
 			cartList.add(book);
 		}
  
 		return Response.ok(cartList).build();
	}
	
	

	@POST
	@Path("/buy")
	@Produces(MediaType.TEXT_PLAIN)
	public Response buy(HttpServletRequest request){
		HttpSession session = request.getSession();
		List cartList = (List)session.getAttribute("CART");
		int size = cartList.size();
		for(int i=0;i<size;i++){
			Book book = (Book)cartList.get(i);
			removeFromCache(book);
		}
		return Response.ok("OK(0)").build();
	}
	
	private void removeFromCache(Book book){
		BookStoreCache  cache =  BookStoreCache.getInstance();
		List list = cache.getDbBooksList();
		Iterator itr = list.iterator();
		Book bookincache = null;
		while(itr.hasNext()){
			bookincache = (Book)itr.next();
			if(bookincache.getBookId()==book.getBookId()){
				break;
			}
		}
		list.remove(bookincache);
		int booksBought = book.getAvailableStock();
		int booksInCache = bookincache.getAvailableStock();
		if(booksInCache>booksBought){
			int booksRemaining = booksInCache - booksBought;
			bookincache.setAvailableStock(booksRemaining);
			list.add(bookincache);
			cache.setDbBooksList(list);
		}
		
		 
		
	}
	
	@POST
	@Path("/removebook/{bookid}/{quantitytoberemoved}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response remove(@PathParam("bookid") String bookId,@PathParam("quantity") String quantitytoberemoved,@Context HttpServletRequest request){
		 
 	 
 		System.out.println("Removing from cart..."+bookId);
 		HttpSession session = request.getSession();
 		List cartList = null;
 		if(session.getAttribute("CART")!=null){
 			 cartList = (List)session.getAttribute("CART");
 		     Iterator itr = cartList.iterator();
 		    Book cartBook = null;
 		     while(itr.hasNext()){
 		    	 cartBook = (Book)itr.next();
 		    	 if(cartBook.getBookId()==Integer.valueOf(bookId)){
 		    		 if(cartBook.getAvailableStock()< Integer.parseInt(quantitytoberemoved)){
 		    			return Response.ok("Books quantity removed are higher than ordered").build();
 		    			
 		    		 }else{
 		    			 int quantityOrdered = cartBook.getAvailableStock();
 		    			 int remainingInCart = quantityOrdered - (Integer.parseInt(quantitytoberemoved));
 		    			 if(remainingInCart==0){
 		    				cartList.remove(cartBook);
 		    			 }else{
 		    				cartList.remove(cartBook);
 		    				 cartBook.setAvailableStock(remainingInCart);
 		    				cartList.add(cartBook);  
 		    			 }
 		    		 }
 		    	 }
 		     }
 		     
 		    // cartList.remove(cartBook);
 		     
 		}
  
 		return Response.ok(cartList).build();
	}
	
	@POST
	@Path("/showcart")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response showcart(HttpServletRequest request){
		HttpSession session = request.getSession();
		 List cartList = (List)session.getAttribute("CART");
		return Response.ok(cartList).build();
	}
}
