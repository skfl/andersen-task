package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.enums.RequestStatus;
import com.andersentask.bookshop.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public void saveRequest(Request request) {
        requestRepository.save(request);
    }
    public void deleteRequest(Long id) {
        requestRepository.delete(id);
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public void deleteRequest(Book book){
        getAllRequests().removeIf(x -> x.equals(book));
    }




}
