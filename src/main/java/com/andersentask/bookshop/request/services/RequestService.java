package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.interfaces.RequestCollectionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RequestService {
    private final RequestCollectionRepository requestRepository;

    public Request saveNewRequest(Request request) {
        return requestRepository.save(request);
    }

    public Optional<Request> getRequestByID(Long id) {
        return requestRepository.findById(id);
    }

    public void deleteRequest(Long id) {
        requestRepository.delete(id);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<Request> getAllRequestsSortedByAmountOfBooks() {
        return getAllRequests().stream()
                .sorted(Comparator.comparing(r -> r.getRequestedBooks().size())).toList();
    }

    public List<Request> getAllRequestsSortedByStatus() {
        return getAllRequests().stream()
                .sorted(Comparator.comparing(Request::getRequestStatus)).toList();
    }

    public List<Request> getAllRequestsSortedByCreationTime() {
        return getAllRequests().stream()
                .sorted(Comparator.comparing(Request::getCreatedAt)).toList();
    }
}
