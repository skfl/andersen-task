package com.andersentask.bookshop.request.services.implementations;

import com.andersentask.bookshop.request.dtos.RequestDTO;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.mappers.RequestMapper;
import com.andersentask.bookshop.request.repositories.RequestRepository;
import com.andersentask.bookshop.request.services.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
private final RequestRepository repository;

    public void saveNewRequest(Request request) {
        repository.save(request);
    }

    public RequestDTO getRequestByID(String id) {
        return RequestMapper.entityToDto(repository.getRequestById(id));
    }

    public void deleteRequest(String id) {
        repository.delete(repository.getRequestById(id));
    }

    @Override
    public List<RequestDTO> getAllRequests() {
        return getRequestDTOS();
    }

    private List<RequestDTO> getRequestDTOS() {
        Iterable<Request> target = repository.findAll();
        List<Request> requests = new ArrayList<>();
        target.forEach(requests::add);
        return RequestMapper.entityListToDtoList(requests);
    }

    @Override
    public List<RequestDTO> getAllRequestsSortedByBook() {
        List<RequestDTO> requestDTOS = getRequestDTOS();
        requestDTOS.sort(Comparator.comparing(Request::getRequestedBook.getName()));
        return requestDTOS;
    }

    @Override
    public List<RequestDTO> getAllRequestsSortedByTotalNumber() {
        List<RequestDTO> requestDTOS = getRequestDTOS();
        requestDTOS.sort(Comparator.comparing(Request::getRequestedBook.getCounter()));
        return requestDTOS;
    }

}
