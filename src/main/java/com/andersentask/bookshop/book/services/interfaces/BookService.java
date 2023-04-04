package com.andersentask.bookshop.book.services.interfaces;

import com.andersentask.bookshop.book.dtos.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();

    List<BookDTO> getBooksSortedByName();

    List<BookDTO> getBooksSortedByPrice();

    List<BookDTO> getBooksSortedByAvailability();
}
