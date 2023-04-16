package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.common.CollectionRepository;
import com.andersentask.bookshop.request.entities.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestRepository implements CollectionRepository<Request, Long> {

    private final List<Request> requests;

    private Long id;

    public RequestRepository() {
        id = 1L;
        this.requests = new ArrayList<>();
    }

    @Override
    public Request save(Request obj) {
        obj.setId(id++);
        requests.add(obj);
        return obj;
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
}
