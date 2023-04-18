package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.user.entities.User;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
public class RequestRepository {

    Request requestToMap(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("id");
            User user = (User) resultSet.getObject("user");
            Book book = (Book) resultSet.getObject("book");
            return Request.builder()
                    .id(id)
                    .user(user)
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

    }


    @Override
    public Optional<Request> findById(Long id) {
        return requests.stream()
                .filter(request -> request.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Request> findAll() {
        return this.requests;
    }

    public void delete(Long id) {
        requests.removeIf(request -> request.getId().equals(id));
    }

    public List<Book> findAllBooksFromAllRequests() {
        return findAll().stream()
                .map(Request::getBook)
                .toList();
    }

    public Long findNumberOfRequestsOnBook(Long bookId) {
        return findAllBooksFromAllRequests().stream()
                .filter(book -> book.getId().equals(bookId))
                .count();
    }
}
