package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class BookRepository {

    private static final String SQL_SELECT_ALL = "select id, name, status, price from books";

    private static final String SQL_SELECT_SORTED_ID = "select id, name, status, price from books order by id";

    private static final String SQL_SELECT_SORTED_NAME = "select id, name, status, price from books order by name";

    private static final String SQL_SELECT_SORTED_STATUS = "select id, name, status, price from books order by status";

    private static final String SQL_SELECT_SORTED_PRICE = "select id, name, status, price from books order by price";

    private static final String SQL_SELECT_BY_ID = "select id, name, status, price from books where id = ?";

    private static final String SQL_INSERT = "insert into books(name, status, price) values (?, ?, ?)";

    private static final String SQL_UPDATE = "update books set name = ?, status = ?, price = ? where id = ?";

    private static final Function<ResultSet, Book> bookMapper = resultSet -> {
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
    private final DataSource dataSource;

    public BookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(bookMapper.apply(resultSet));
                }
            }
            return books;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Book save(Book book) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getStatus().toString());
            statement.setBigDecimal(3, book.getPrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Can't insert book");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return findById(generatedKeys.getLong("id"))
                            .orElseThrow(() -> new IllegalArgumentException("Something goes wrong while book insertion"));
                } else {
                    throw new SQLException("Can't get id");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Optional<Book> findById(Long bookId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(bookMapper.apply(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
        List<Book> sortedBooks = new ArrayList<>();
        String sqlSort = SQL_SELECT_BY_ID;
        switch (bookSort) {
            case ID -> sqlSort = SQL_SELECT_SORTED_ID;
            case NAME -> sqlSort = SQL_SELECT_SORTED_NAME;
            case PRICE -> sqlSort = SQL_SELECT_SORTED_PRICE;
            case STATUS -> sqlSort = SQL_SELECT_SORTED_STATUS;
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlSort)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sortedBooks.add(bookMapper.apply(resultSet));
                }
            }
            return sortedBooks;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public void update(Book book) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getStatus().toString());
            statement.setBigDecimal(3, book.getPrice());
            statement.setLong(4, book.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't update book");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}
