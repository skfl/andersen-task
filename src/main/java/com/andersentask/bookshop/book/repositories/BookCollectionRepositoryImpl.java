package com.andersentask.bookshop.book.repositories;

import com.andersentask.bookshop.book.common.AbstractCollectionRepository;
import com.andersentask.bookshop.book.entities.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCollectionRepositoryImpl implements AbstractCollectionRepository<Book, Long> {
    private final List<Book> books;

    public BookCollectionRepositoryImpl() {
        this.books = new ArrayList<>();
    }

    @Override
    public Book save(Book obj) {
        books.add(obj);
        return obj;
    }

    @Override
    public void delete(Long id) {
        for (Book book : books){
            if (book.getId().equals(id)){
                books.remove(book);
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        return this.books;
    }
}
