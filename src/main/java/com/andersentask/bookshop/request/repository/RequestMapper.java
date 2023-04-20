package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.request.entities.Request;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Slf4j
class RequestMapper {

    static final Function<ResultSet, Request> mapper = resultSet -> {
        try {
            Long id = resultSet.getLong("id");
            Long bookId = resultSet.getLong("book_id");
//            Book book = bookService.getBookById(bookId).orElseThrow();
            return Request.builder()
                    .id(id)
                    .build();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    };
}
