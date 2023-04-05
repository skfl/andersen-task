package com.andersentask.bookshop.request.repositories;

import com.andersentask.bookshop.request.entities.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<Request, String> {
    Request getRequestById(String id);
}
