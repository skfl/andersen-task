package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> setStatusToBook(Long id, BookStatus bookStatus) {
        Optional<Book> book = getBookById(id);
        book.ifPresent(value -> value.setStatus(bookStatus));
        return book;
    }

    public List<Book> getSortedBooks(BookSort bookSort) {
        List<Book> books = getAllBooks();
        List<Book> booksToReturn = new ArrayList<>();
        switch (bookSort) {
            case NAME -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(Book::getName))
                    .toList();
            case PRICE -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(Book::getPrice))
                    .toList();
            case STATUS -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(x -> x.getStatus().ordinal()))
                    .toList();
            case ID -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(Book::getId))
                    .toList();
        }
        return booksToReturn;
    }

    public List<Book> getBooksByIds(List<Long> ids) {
        List<Book> books = new ArrayList<>();
        for (Long id : ids) {
            Optional<Book> optionalBook = getBookById(id);
            optionalBook.ifPresent(books::add);
        }
        return books;
    }

    public List<Book> getBooksOutOfStock(List<Book> books) {
        return books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();
    }

    public boolean allBooksAreAvailable(List<Book> books) {
        return books.stream()
                .allMatch(x -> x.getStatus().equals(BookStatus.AVAILABLE));
    }
}



