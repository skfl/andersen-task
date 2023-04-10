package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.common.SetUpRequest;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.enums.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RequestRepositoryTest {
    @Mock
    private RequestRepository requestRepository;

    private SetUpRequest setUpRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestRepository = new RequestRepository();
        this.setUpRequest = new SetUpRequest();
    }

    @Test
    void save() {
        Request savedRequest1 = requestRepository.save(setUpRequest.setUpRequestWithOutOfStockBook());

        assertEquals(1, requestRepository.findAll().size());
        assertNotNull(requestRepository.findAll().get(0).getId());
        assertEquals(savedRequest1, requestRepository.findAll().get(0));

        requestRepository.save(setUpRequest.setUpRequestWithOutOfStockBook());
        assertEquals(2, requestRepository.findAll().get(1).getId());
    }

    @Test
    void delete() {
        requestRepository.save(setUpRequest.setUpRequestWithOutOfStockBook());
        requestRepository.delete(1L);
        assertEquals(RequestStatus.CANCELED, requestRepository.findAll().get(0).getRequestStatus());
    }

    @Test
    void findById() {
        Request requestTest = requestRepository.save(setUpRequest.setUpRequestWithOutOfStockBook());

        Optional<Request> requestResult1 = requestRepository.findById(requestTest.getId());
        assertTrue(requestResult1.isPresent());
        assertEquals(requestTest, requestResult1.get());

        Optional<Request> requestResult2 = requestRepository.findById(10L);
        assertFalse(requestResult2.isPresent());
    }

    @Test
    void findAll() {
        Request request1 = requestRepository.save(setUpRequest.setUpRequestWithNoBooks());
        Request request2 = requestRepository.save(setUpRequest.setUpRequestWithNoBooks());

        assertEquals(2, requestRepository.findAll().size());
        assertTrue(requestRepository.findAll().contains(request1));
        assertTrue(requestRepository.findAll().contains(request2));
    }
}