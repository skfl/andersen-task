package com.andersentask.bookshop.request.facade;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.entities.enums.RequestStatus;
import com.andersentask.bookshop.user.entities.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class RequestFacade {

    public Request buildRequest(User user, List<Book> requestedBooks) {

        return Request.builder()
                .user(user)
                .requestedBooks(requestedBooks)
                .createdAt(LocalDateTime.now())
                .requestStatus(RequestStatus.IN_PROCESSING)
                .build();
    }
}
