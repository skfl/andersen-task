package com.andersentask.bookshop.request.repository;

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
    public void delete(Long id) {
        requests.removeIf(request -> request.getId().equals(id));
    }

    @Override
    public Optional<Request> findById(Long id) {
        for (Request request : requests) {
            if (request.getId().equals(id)) {
                return Optional.of(request);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Request> findAll() {
        return this.requests;
    }
}
