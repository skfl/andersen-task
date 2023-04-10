package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.common.AbstractCollectionRepository;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestRepository implements AbstractCollectionRepository<Request, Long> {

    private final List<Request> requests;

    private Long id;

    public RequestRepository() {
        this.requests = new ArrayList<>();
        id = 1L;
    }

    @Override
    public Request save(Request obj) {
        obj.setId(id++);
        requests.add(obj);
        return obj;
    }

    @Override
    public void delete(Long id) {
       findById(id)
                .ifPresentOrElse(x -> x.setRequestStatus(RequestStatus.CANCELED), () -> {
                });
    }

    //todo
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
