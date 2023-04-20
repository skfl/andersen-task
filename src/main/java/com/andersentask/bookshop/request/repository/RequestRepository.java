package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.request.entities.Request;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RequestRepository {

    private final DataSource dataSource;
    private final BookService bookService;

    public RequestRepository(DataSource dataSource, BookService bookService) {
        this.dataSource = dataSource;
        this.bookService = bookService;
    }

    public Request save(Request request) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(RequestSQLCommands.SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, 0L);
            if (request.getUser() != null) {
                statement.setLong(1, request.getUser().getId());
            }
            statement.setLong(2, request.getBook().getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Can't insert request");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return findById(generatedKeys.getLong("id"))
                            .orElseThrow(() -> new IllegalArgumentException("Something goes wrong while request insertion"));
                } else {
                    throw new SQLException("Can't get it");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Optional<Request> findById(Long requestId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(RequestSQLCommands.SQL_SELECT_BY_ID)) {
            statement.setLong(1, requestId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildRequestAndsetBook(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public List<Request> findAll() {
        List<Request> requests = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(RequestSQLCommands.SQL_SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    requests.add(buildRequestAndsetBook(resultSet));
                }
            }
            return requests;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private Request buildRequestAndsetBook(ResultSet resultSet) {
        try {
            Request request = RequestMapper.mapper.apply(resultSet);
            Book bookInRequest = bookService.getBookById(resultSet.getLong("book_id"))
                    .orElseThrow();
            request.setBook(bookInRequest);
            return request;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void delete(Book book) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(RequestSQLCommands.SQL_DELETE_BY_ID)) {
            statement.setLong(1, book.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Long findNumberOfRequestsOnBook(Long bookId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(RequestSQLCommands.SQL_COUNT_BY_BOOK_ID)) {
            statement.setLong(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("count");
            }
            return 0L;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}
