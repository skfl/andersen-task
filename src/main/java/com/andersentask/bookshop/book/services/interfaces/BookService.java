package com.andersentask.bookshop.book.services.interfaces;

import com.andersentask.bookshop.book.dtos.BookDTO;
import com.andersentask.bookshop.book.entities.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<BookDTO> getAllBooks();

    List<BookDTO> getBooksSortedByName();

    List<BookDTO> getBooksSortedByPrice();

    List<BookDTO> getBooksSortedByAvailability();
}
