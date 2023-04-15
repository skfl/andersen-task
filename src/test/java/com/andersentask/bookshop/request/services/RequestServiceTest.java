package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.RequestRepository;
import com.andersentask.bookshop.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    private RequestService requestService;

    @BeforeEach
    void setUp() {
        requestRepository = new RequestRepository();
        requestService = new RequestService(requestRepository);
    }

    private final Request testRequest = Request.builder()
            .user(User.builder()
                    .firstName("qwerty")
                    .id(1L)
                    .build())
            .book(Book.builder()
                    .id(1L)
                    .name("123")
                    .build())
            .build();

    private final Request testRequest2 = Request.builder()
            .user(User.builder()
                    .firstName("qwerty2")
                    .build())
            .book(Book.builder()
                    .id(2L)
                    .name("1234")
                    .build())
            .build();

    private final Request testRequest3 = Request.builder()
            .user(User.builder()
                    .firstName("qwerty3")
                    .build())
            .book(Book.builder()
                    .id(1L)
                    .name("12345")
                    .build())
            .build();

    @Test
    void saveRequest() {
        requestService.saveRequest(testRequest);

        assertNotNull(testRequest.getId());
        assertEquals(Optional.of(testRequest), requestRepository.findById(testRequest.getId()));
        assertEquals(1, requestService.getAllRequests().size());
        assertTrue(requestService.getAllRequests().contains(testRequest));
    }

    @Test
    void getAllRequests() {
        requestService.saveRequest(testRequest);
        requestService.saveRequest(testRequest2);

        List<Request> requestList = requestService.getAllRequests();

        assertEquals(2, requestService.getAllRequests().size());
        assertTrue(requestList.contains(testRequest));
        assertTrue(requestList.contains(testRequest2));
    }

    @Test
    void deleteRequest() {
        requestService.saveRequest(testRequest);
        requestService.deleteRequest(testRequest.getBook());

        assertTrue(requestRepository.findAll().isEmpty());
    }

    @Test
    void getAllBooksFromRequests() {
        requestService.saveRequest(testRequest);
        requestService.saveRequest(testRequest2);
        requestService.saveRequest(testRequest3);

        List<Book> bookList = requestService.getAllRequests().stream().map(Request::getBook).toList();

        assertEquals(bookList, requestService.getAllBooksFromRequests());
    }

    @Test
    void getNumberOfRequestsOnBook() {
        requestService.saveRequest(testRequest);
        requestService.saveRequest(testRequest2);


        assertEquals(1, requestService.getNumberOfRequestsOnBook(testRequest2.getBook().getId()));
        assertEquals(1, requestService.getNumberOfRequestsOnBook(testRequest.getBook().getId()));

        requestService.saveRequest(testRequest3);

        assertEquals(2, requestService.getNumberOfRequestsOnBook(testRequest.getBook().getId()));
    }
}