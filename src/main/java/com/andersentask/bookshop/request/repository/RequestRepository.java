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

    private static final String SQL_COUNT_BY_BOOK_ID = "select count() as count from requests where book_id =?";
    BookService bookService;
    private static final String SQL_INSERT = "insert into requests(id, user_id, book_id) values (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "select * from requests where id = ?";
    private static final String SQL_SELECT_ALL = "select * from requests";
    private static final String SQL_DELETE_BY_ID = "delete from requests where id = ?";

    Request requestToMap(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("id");
            Long bookId = resultSet.getLong("book_id");
            Book book = bookService.getBookById(bookId).orElseThrow();
            return Request.builder()
                    .id(id)
                    .book(book)
                    .build();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private final DataSource dataSource;

    public RequestRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Request save(Request request) {
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, request.getId());
            statement.setLong(2, 0L);
            if (request.getUser() != null) {
                statement.setLong(2, request.getUser().getId());
            }
            statement.setLong(3, request.getBook().getId());
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
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, requestId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(requestToMap(resultSet));
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
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    requests.add(requestToMap(resultSet));
                }
            }
            return requests;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public void delete(Book book) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            statement.setLong(1, book.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public Long findNumberOfRequestsOnBook(Long bookId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_COUNT_BY_BOOK_ID);
             ResultSet resultSet = statement.executeQuery()) {
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
