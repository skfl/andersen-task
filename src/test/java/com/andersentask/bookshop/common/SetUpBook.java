package com.andersentask.bookshop.common;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;

import java.util.ArrayList;
import java.util.List;

public class SetUpBook {

    private List<Book> bookDB;

    SetUpBook(){
        this.bookDB = setUpBooks();
    }

    private List<Book> setUpBooks() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder()
                .name("qwerty1")
                .price(10.00)
                .status(BookStatus.OUT_OF_STOCK)
                .build());
        books.add(Book.builder()
                .name("qwerty2")
                .price(70.00)
                .status(BookStatus.AVAILABLE)
                .build());
        books.add(Book.builder()
                .name("qwerty3")
                .price(5.00)
                .status(BookStatus.AVAILABLE)
                .build());
        books.add(Book.builder()
                .name("qwerty4")
                .price(30.00)
                .status(BookStatus.AVAILABLE)
                .build());
        books.add(Book.builder()
                .name("qwerty5")
                .price(9.00)
                .status(BookStatus.OUT_OF_STOCK)
                .build());
        books.add(Book.builder()
                .name("qwerty6")
                .price(200.00)
                .status(BookStatus.OUT_OF_STOCK)
                .build());
        books.add(Book.builder()
                .name("qwerty7")
                .price(2.00)
                .status(BookStatus.OUT_OF_STOCK)
                .build());
        books.add(Book.builder()
                .name("qwerty8")
                .price(3.00)
                .status(BookStatus.AVAILABLE)
                .build());
        return books;
    }

    public List<Book> getListOfOutOfStockOneBook() {
        List<Book> books = new ArrayList<>();
        books.add(bookDB.get(0));
        return books;
    }

    public List<Book> getEmptyListOfBooks() {
        return new ArrayList<>();
    }

    public List<Book> getListOfAvailableOneBook() {
        List<Book> books = new ArrayList<>();
        books.add(bookDB.get(1));
        return books;
    }
}
