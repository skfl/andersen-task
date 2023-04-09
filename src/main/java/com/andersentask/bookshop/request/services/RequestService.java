package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.entities.enums.RequestStatus;
import com.andersentask.bookshop.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    private final BookService bookService;

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

    public List<Request> checkRequestsToOrder() {
        List<Request> requestList = getAllRequests().stream()
                .filter(r -> r.getRequestStatus().equals(RequestStatus.IN_PROCESSING))
                .filter(r -> bookService.checkListOfBooksOnAvailability(r.getRequestedBooks()))
                .toList();
        for (Request request : requestList) {
            request.setRequestStatus(RequestStatus.TO_ORDER);
        }
        return requestList;
    }
}
