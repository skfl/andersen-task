package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class BookRepository {

    private final EntityManager entityManager;

    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery(BookJPQLQueries.SELECT_ALL, Book.class);
        return query.getResultList();
    }

    //toDo: maybe delete this method? We have no such method in commands
    public Book save(Book book) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(book);
        transaction.commit();
        return book;
    }

    public Optional<Book> findById(Long bookId) {
        TypedQuery<Book> query = entityManager.createQuery(BookJPQLQueries.SELECT_BY_ID, Book.class);
        query.setParameter("id", bookId);
        return query.getResultStream().findFirst();
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
        String queryString = "";
        switch (bookSort) {
            case NAME -> queryString = BookJPQLQueries.SELECT_SORTED_BY_NAME;
            case ID -> queryString = BookJPQLQueries.SELECT_SORTED_BY_ID;
            case STATUS -> queryString = BookJPQLQueries.SELECT_SORTED_BY_STATUS;
            case PRICE -> queryString = BookJPQLQueries.SELECT_SORTED_BY_PRICE;
        }
        TypedQuery<Book> query = entityManager.createQuery(queryString, Book.class);
        return query.getResultList();
    }

    public void update(Book book) {
        Book bookToUpdate = entityManager.getReference(Book.class, book.getId());
        bookToUpdate.setStatus(book.getStatus());
        bookToUpdate.setName(book.getName());
        bookToUpdate.setPrice(book.getPrice());
        entityManager.merge(bookToUpdate);
    }
}
