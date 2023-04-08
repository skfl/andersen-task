package com.andersentask.bookshop.request.repository.interfaces;

import com.andersentask.bookshop.request.entities.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestCollectionRepository extends AbstractCollectionRepository<Request, Long> {

}
