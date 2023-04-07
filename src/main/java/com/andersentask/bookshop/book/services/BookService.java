package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.repositories.interfaces.BookCollectionRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookService {

    private final BookCollectionRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksSortedByName() {
        List<Book> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(x -> x.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksSortedByPrice() {
        List<Book> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(Book::getPrice))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksSortedByAvailability() {
        List<Book> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparingInt(x -> x.getStatus().getOrdinal()))
                .collect(Collectors.toList());
    }
}

