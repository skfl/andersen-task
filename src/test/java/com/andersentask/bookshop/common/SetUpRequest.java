package com.andersentask.bookshop.common;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.enums.RequestStatus;
import com.andersentask.bookshop.user.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SetUpRequest {

    private final SetUpBook setUpBook = new SetUpBook();



    public Request setUpRequestWithOutOfStockBook() {
        List<Book> books = setUpBook.getListOfOutOfStockOneBook();
        return Request.builder()
                .requestStatus(RequestStatus.IN_PROCESS)
                .createdAt(LocalDateTime.now())
                .user(User.builder().build())
                .requestedBooks(books)
                .build();
    }

    public Request setUpRequestWithNoBooks() {
        return Request.builder()
                .requestStatus(RequestStatus.IN_PROCESS)
                .createdAt(LocalDateTime.now())
                .user(User.builder().build())
                .requestedBooks(new ArrayList<>())
                .build();
    }
}
