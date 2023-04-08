package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.interfaces.RequestCollectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestCollectionRepositoryImpl implements RequestCollectionRepository {

    private final List<Request> requests;

    public RequestCollectionRepositoryImpl() {
        this.requests = new ArrayList<>();
    }

    @Override
    public Request save(Request obj) {
        requests.add(obj);
        return obj;
    }

    @Override
    public void delete(Long id) {
        for (Request request : requests){
            if (request.getId().equals(id)){
                requests.remove(request);
            }
        }
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
