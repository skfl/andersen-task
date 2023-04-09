package com.andersentask.bookshop.book.services;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

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

    public List<Book> getBooksSortedByName() {
        List<Book> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(x -> x.getName().toLowerCase()))
                .toList();
    }

    public List<Book> getBooksSortedByPrice() {
        List<Book> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(Book::getPrice))
                .toList();
    }

    public List<Book> getBooksSortedByAvailability() {
        List<Book> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparingInt(x -> x.getStatus().ordinal()))
                .toList();
    }

    public Book changeBookToOutOfStock(Long id) {
        return bookRepository.save(null);
    }

    public boolean checkListOfBooksOnAvailability(List<Book> books) {
        for (Book book : books) {
            Optional<Book> foundBook = bookRepository.findById(book.getId());
            if (!foundBook.get().getStatus().equals(BookStatus.AVAILABLE)) {
                return false;
            }
        }
        return true;
    }

    public double getCostOfListOfBooks(List<Book> books) {
        return books.stream()
                .map(Book::getPrice)
                .reduce(0D, Double::sum);
    }

    public List<Book> getOnlyAvailableBooks(List<Book> books) {
        return books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.AVAILABLE))
                .toList();
    }

    public List<Book> getOnlyOutOfStockBooks(List<Book> books) {
        return books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();
    }

}

