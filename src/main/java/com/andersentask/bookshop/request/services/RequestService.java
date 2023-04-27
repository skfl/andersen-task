package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestService {

    private final RequestRepository requestRepository;

    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public void deleteRequest(Book book) {
        requestRepository.removeByBook(book);
    }

    public List<Book> getAllBooksFromAllRequests() {
        return getAllRequests().stream()
                .map(Request::getBook)
                .toList();
    }

    public Long getNumberOfRequestsOnBook(Book book) {
        return requestRepository.countByBook(book);
    }
}
