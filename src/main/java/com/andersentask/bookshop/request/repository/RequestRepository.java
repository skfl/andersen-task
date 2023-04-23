package com.andersentask.bookshop.request.repository;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.request.entities.Request;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RequestRepository {

    private final EntityManager entityManager;

    public RequestRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Request save(Request request) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(request);
        transaction.commit();
        return request;
    }

    public Optional<Request> findById(Long requestId) {
        TypedQuery<Request> query = entityManager.createQuery(RequestJPQLQueries.SQL_SELECT_BY_ID,Request.class);
        query.setParameter("id",requestId);
        return query.getResultStream().findFirst();
    }

    public List<Request> findAll() {
        TypedQuery<Request> query = entityManager.createQuery(RequestJPQLQueries.SQL_SELECT_ALL,Request.class);
        return query.getResultList();
    }

    public void delete(Book book) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query = entityManager.createQuery(RequestJPQLQueries.SQL_DELETE_BY_ID);
        query.setParameter("book",book);
        query.executeUpdate();
        transaction.commit();
    }

    public Long findNumberOfRequestsOnBook(Book book) {
        TypedQuery<Long> query = entityManager.createQuery(RequestJPQLQueries.SQL_COUNT_BY_BOOK_ID,Long.class);
        query.setParameter("book", book);
        return query.getSingleResult();
    }
}
