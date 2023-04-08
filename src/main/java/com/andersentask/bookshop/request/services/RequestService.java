package com.andersentask.bookshop.request.services;

import com.andersentask.bookshop.request.dtos.RequestDTO;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.exceptions.ErrorMessage;
import com.andersentask.bookshop.request.exceptions.NoAnyRecordException;
import com.andersentask.bookshop.request.exceptions.RequestNotExistsException;
import com.andersentask.bookshop.request.mappers.RequestMapper;
import com.andersentask.bookshop.request.repository.RequestCollectionRepositoryImpl;
import com.andersentask.bookshop.request.repository.interfaces.RequestCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class RequestService {
    private final RequestCollectionRepository requestRepository;

    public Request saveNewRequest(Request request) {
        return requestRepository.save(request);
    }

    public Request getRequestByID(Long id) {
        return requestRepository.findById(id);
    }

    public void deleteRequest(Long id) {
        requestRepository.delete(id);
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public List<Request> getAllRequestsSortedByBook() {
        List<Request> requests = getAllRequests();
        return requests.stream()
                .sorted(Comparator.comparing(Request::));
    }

    @Override
    public List<RequestDTO> getAllRequestsSortedByTotalNumber() {
        List<RequestDTO> requestDTOS = getRequestDTOS();
        requestDTOS.sort(Comparator.comparing(Request::getRequestedBooks.getCounter()));
        return requestDTOS;
    }
}
