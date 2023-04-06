package com.andersentask.bookshop.request.repositories;

import com.andersentask.bookshop.request.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
    Optional<Request> getRequestById(Long id);

    List<Request> findAll();
}
