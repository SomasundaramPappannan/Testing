package com.my.bookstore.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.my.bookstore.client.BookStoreRestClient;
import com.my.bookstore.core.Book;

public class BookStoreMain {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static BookStoreRestClient bookClient = null;

    public static void main(String[] args) throws Exception {
        BookStoreMain mainObj = new BookStoreMain();
        mainObj.login();
        mainObj.displayAvailableBooksOrSearch();
    }

    private void login() throws Exception {
        System.out.println("Enter your name \n");
        String userName = br.readLine();
        System.out.println("Enter your emailid \n");
        String emailId = br.readLine();
        new BookStoreRestClient().login(userName, emailId);
    }

    private void displayAvailableBooksOrSearch() throws Exception {
        System.out.println("Type 1 to display all books ");
        System.out.println("Type 2 to Search the books \n");
        String s = br.readLine();
        BookStoreRestClient client = new BookStoreRestClient();
        if ("1".equals(s)) {
            displayAll();
        } else if ("2".equals(s)) {
            System.out.println("Please Enter to search ");
            String searchTerm = br.readLine();
            displayBookDetails(client.search(searchTerm));
        } else {
            System.out.println("Wrong option entered.. Please try again!!");
            displayAvailableBooksOrSearch();
        }
    }

    private void displayAll() throws Exception {
        System.out.println("**************Available Books************");
        for (Book book : getBookStoreClient().displayAll()) {
            System.out.println(book.getBookId() + "." + book.getTitle() + " " + book.getAuthor());
        }
        System.out.println("Please choose one book by entering serial.no");
        displayBookDetails(getBookStoreClient().getBook(br.readLine()));
    }

    private void displayBookDetails(Book book) throws Exception {
        if (book != null) {
            System.out.println("****************************************");
            System.out.println("Book Name::" + book.getTitle());
            System.out.println("Book Author::" + book.getAuthor());

            System.out.println("Book Price::" + book.getPrice());
            System.out.println("Avalible Qunatity::" + book.getAvailableStock());
            System.out.println("****************************************");

            System.out.print("Please Enter how many quantity you wish to buy\n ");
            String qty = br.readLine();
            int availableStock = book.getAvailableStock();
            String price = book.getPrice();
            int iQty = Integer.parseInt(qty);
            boolean optionSelection = false;
            if (availableStock == 0) {
                System.out.print("The Stock is Not Available ..!.");
                System.out.print("Please enter 1 to purchase another book....\n ");
                System.out.print("Please enter 2 to quit..");
                String choice = br.readLine();
                if (choice.equals("1")) {
                    displayAvailableBooksOrSearch();
                } else if (choice.equals("2")) {
                    System.exit(0);
                } else {
                    System.out.print("You Have entered wrong option..!. Quiting .Please login again");
                }
            } else if (iQty > availableStock) {
                while (!optionSelection) {
                    System.out.print("Please enter less than Avalible Stock \n");
                    System.out.print("Please Enter how many quantity you wish to buy\n ");
                    String qty1 = br.readLine();
                    if (Integer.parseInt(qty1) > availableStock) {
                        optionSelection = false;
                    } else {
                        int iQty1 = Integer.parseInt(qty1);
                        buy(iQty1, price);
                    }

                }
            } else {
                buy(iQty, price);
            }
        } else {
            System.out.println("No book found!!");
        }
        // addBookToCart(book , iQty, br, client);

    }

    private void addBookToCart(Book book, int count, int iQty, String price) throws Exception {
        List cartList = getBookStoreClient().addBookToCart(book.getBookId(), count);
        Iterator itr = cartList.iterator();
        System.out.println("************Books available in your cart*********");
        while (itr.hasNext()) {
            Book cartBook = (Book) itr.next();
            System.out.println(cartBook.getBookId() + " " + cartBook.getTitle() + " " + cartBook.getPrice() + " " + cartBook.getAvailableStock());
        }
        System.out.print("Please enter 1 to buy..\n ");
        System.out.print("Please enter 2 to remove this book from cart..");
        System.out.print("Please enter 3 to purchase more..");
        String choice = br.readLine();
        if (choice.equals("1")) {
            buy(iQty, price);
        } else if (choice.equals("2")) {
            removeBookFromCart(book, count, iQty, price);
        } else if (choice.equals("3")) {
            displayAvailableBooksOrSearch();
        }

    }

    private void removeBookFromCart(Book book, int count, int iQty, String price) throws Exception {
        List cartList = getBookStoreClient().removeBookFromCart(book.getBookId(), count);
        System.out.print("Please enter 1 to buy..\n ");
        System.out.print("Please enter 2 to purchase another book..");
        String choice = br.readLine();
        if (choice.equals("1")) {
            buy(iQty, price);
        } else if (choice.equals("2")) {
            displayAvailableBooksOrSearch();
        }
    }

    private void buy(int iQty, String price) throws Exception {
        double totalPrice = getBookStoreClient().buy(iQty, price);
        System.out.print("The Total Price is ::: " + totalPrice);
        System.out.print("Please enter 1 to purchase another book....\n ");
        System.out.print("Please enter 2 to quit..");
        String choice = br.readLine();
        if (choice.equals("1")) {
            displayAvailableBooksOrSearch();
        } else if (choice.equals("2")) {
            System.exit(0);
        } else {
            System.out.print("You have entered wrong Option..");
        }

    }

    private BookStoreRestClient getBookStoreClient() {
        if (bookClient == null) {
            bookClient = new BookStoreRestClient();
        }
        return bookClient;
    }

}
