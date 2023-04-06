package com.andersentask.bookshop.request.services.implementations;

import com.andersentask.bookshop.request.dtos.RequestDTO;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.exceptions.ErrorMessage;
import com.andersentask.bookshop.request.exceptions.NoAnyRecordException;
import com.andersentask.bookshop.request.exceptions.RequestNotExistsException;
import com.andersentask.bookshop.request.mappers.RequestMapper;
import com.andersentask.bookshop.request.repositories.RequestRepository;
import com.andersentask.bookshop.request.services.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;

    public void saveNewRequest(Request request) {
        repository.save(request);
    }

    public RequestDTO getRequestByID(Long id) {
        return RequestMapper.entityToDto(repository.getRequestById(id)
                .orElseThrow(() -> new RequestNotExistsException(ErrorMessage.REQUEST_NOT_EXISTS)));
    }

    public void deleteRequest(Long id) {
        repository.delete(repository.getRequestById(id)
                .orElseThrow(() -> new RequestNotExistsException(ErrorMessage.REQUEST_NOT_EXISTS)));
    }

    @Override
    public List<RequestDTO> getAllRequests() {
        return getRequestDTOS();
    }

    private List<RequestDTO> getRequestDTOS() {
        var target = repository.findAll();
        if (target.isEmpty()) {
            throw new NoAnyRecordException(ErrorMessage.NO_REQUESTS);
        }
        return RequestMapper.entityListToDtoList(target);
    }

    @Override
    public List<RequestDTO> getAllRequestsSortedByBook() {
        List<RequestDTO> requestDTOS = getRequestDTOS();
        requestDTOS.sort(Comparator.comparing(Request::getRequestedBooks.getName()));
        return requestDTOS;
    }

    @Override
    public List<RequestDTO> getAllRequestsSortedByTotalNumber() {
        List<RequestDTO> requestDTOS = getRequestDTOS();
        requestDTOS.sort(Comparator.comparing(Request::getRequestedBooks.getCounter()));
        return requestDTOS;
    }
}
