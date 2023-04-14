package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RequestRepositoryTest {

    @Mock
    private RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        requestRepository = new RequestRepository();
    }

    private final Request testRequest = Request.builder()
            .user(User.builder().firstName("qwerty").build())
            .book(Book.builder().name("123").build())
            .build();

    @Test
    void save() {
        requestRepository.save(testRequest);

        assertNotNull(testRequest.getId());
        assertEquals(Optional.of(testRequest), requestRepository.findById(1L));
        assertEquals(1, requestRepository.findAll().size());
        assertTrue(requestRepository.findAll().contains(testRequest));
    }

    @Test
    void delete() {
        requestRepository.save(testRequest);
        requestRepository.delete(testRequest.getId());

        assertTrue(requestRepository.findAll().isEmpty());
    }

    @Test
    void findById() {
        requestRepository.save(testRequest);
        Optional<Request> result = requestRepository.findById(testRequest.getId());

        assertTrue(result.isPresent());
        assertEquals(testRequest, result.get());

        assertEquals(Optional.empty(), requestRepository.findById(2L));
    }

    @Test
    void findAll() {
        Request testRequest2 = Request.builder()
                .user(User.builder().firstName("qwerty2").build())
                .book(Book.builder().name("1234").build())
                .build();

        requestRepository.save(testRequest);
        requestRepository.save(testRequest2);

        List<Request> result = requestRepository.findAll();
        assertEquals(2, requestRepository.findAll().size());
        assertTrue(result.contains(testRequest));
        assertTrue(result.contains(testRequest2));
    }
}