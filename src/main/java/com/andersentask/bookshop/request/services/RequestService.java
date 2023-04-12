package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Optional;


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
        getAllRequests().removeIf(x -> x.getBook().equals(book));
    }

    public List<Book> getAllBooksFromRequests(){
        return getAllRequests().stream()
                .map(Request::getBook)
                .toList();
    }

    public Long getNumberOfRequestsOnBook(Long id) {
        return getAllBooksFromRequests().stream()
                .filter(x -> x.getId().equals(id))
                .count();
    }


}
