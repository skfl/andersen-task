package com.andersentask.bookshop.request.entities;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.enums.RequestStatus;
import com.andersentask.bookshop.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    private Long id;

    private User user;

    private Book book;

    //toDo: to delete (discuss). Specification has no request status
    private RequestStatus requestStatus;
    //toDo: to delete (discuss). Specification has no request date
    private LocalDateTime createdAt;
}
