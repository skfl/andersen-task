package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.common.SetUpRequest;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.enums.RequestStatus;
import com.andersentask.bookshop.request.repository.RequestRepository;
import com.andersentask.bookshop.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
    private RequestService requestService;

    private BookService bookService;

    private SetUpRequest setUpRequest;

    private List<Book> books = new ArrayList<>();

    private Request request;

    RequestServiceTest() {
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(new BookRepository());
        requestRepository = new RequestRepository();
        requestService = new RequestService(requestRepository, bookService);
        this.setUpRequest = new SetUpRequest();
        books.add(new Book(1L,"123", BookStatus.AVAILABLE, 10.0));
        request = Request.builder()
                .id(1L)
                .user(User.builder().build())
                .requestStatus(RequestStatus.IN_PROCESS)
                .createdAt(LocalDateTime.now())
                .requestedBooks(books)
                .build();
    }


    @Test
    void saveRequest() {
        requestService.saveRequest(setUpRequest.setUpRequestWithOutOfStockBook());
        assertFalse(requestRepository.findAll().isEmpty());
    }

    @Test
    void getRequestByID() {
//        requestService.saveRequest(request);
        requestRepository.save(request);


        System.out.println(requestRepository.findAll());
        assertFalse(requestRepository.findAll().isEmpty());

//        Optional<Request> requestResult1 = requestService.getRequestByID(1L);
//        assertTrue(requestResult1.isEmpty());
//
//        Optional<Request> requestResult2 = requestRepository.findById(10L);
//        assertFalse(requestResult2.isPresent());
    }

    @Test
    void cancelRequest() {
    }

    @Test
    void getAllRequests() {
    }

    @Test
    void getAllRequestsSortedByAmountOfBooks() {
    }

    @Test
    void getAllRequestsSortedByStatus() {
    }

    @Test
    void getAllRequestsSortedByCreationTime() {
    }

    @Test
    void getRequestsThatReadyToOrder() {
    }
}