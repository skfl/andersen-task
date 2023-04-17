package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public void deleteRequest(Book book) {
        getAllRequests().removeIf(request -> request.getBook().equals(book));
    }

    public List<Book> getAllBooksFromAllRequests() {
        return requestRepository.findAllBooksFromAllRequests();
    }

    public Long getNumberOfRequestsOnBook(Long bookId) {
        return requestRepository.findNumberOfRequestsOnBook(bookId);
    }
}
