package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Slf4j
public class BookMapper {
    static final Function<ResultSet, Book> bookMapper = resultSet -> {
        try {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            BookStatus status = BookStatus.valueOf(resultSet.getString("status"));
            BigDecimal price = resultSet.getBigDecimal("price");
            return Book.builder()
                    .id(id)
                    .name(name)
                    .status(status)
                    .price(price)
                    .build();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    };

    private BookMapper() {

    }
}
