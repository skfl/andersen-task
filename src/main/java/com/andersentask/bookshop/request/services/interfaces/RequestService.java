package com.andersentask.bookshop.request.services.interfaces;

import com.andersentask.bookshop.request.dtos.RequestDTO;
import com.andersentask.bookshop.request.entities.Request;

import java.util.List;

public interface RequestService {

    void saveNewRequest(Request request);

    RequestDTO getRequestByID(Long id);

    void deleteRequest(Long id);

    List<RequestDTO> getAllRequests();

    List<RequestDTO> getAllRequestsSortedByBook();

    List<RequestDTO> getAllRequestsSortedByTotalNumber();
}
