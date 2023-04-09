package com.andersentask.bookshop.request.entities;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.enums.RequestStatus;
import com.andersentask.bookshop.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    private Long id;

    private User user;

    private List<Book> requestedBooks;

    private RequestStatus requestStatus;

    private LocalDateTime createdAt;

}
