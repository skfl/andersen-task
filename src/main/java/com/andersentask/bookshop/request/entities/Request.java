package com.andersentask.bookshop.request.entities;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    private Long id;

    private User user;

    private Book book;
}
