package com.andersentask.bookshop.services.implementations;

import com.andersentask.bookshop.dtos.BookDTO;
import com.andersentask.bookshop.entities.Book;
import com.andersentask.bookshop.enums.BookStatus;
import com.andersentask.bookshop.mappers.BookMapper;
import com.andersentask.bookshop.repositories.BookRepository;
import com.andersentask.bookshop.services.interfaces.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        return BookMapper.entityListToDtoList(bookRepository.findAll());
    }

    @Override
    public List<BookDTO> getBooksSortedByName() {
        List<BookDTO> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(BookDTO::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksSortedByPrice() {
        List<BookDTO> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparingInt(BookDTO::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksSortedByAvailability() {
        List<BookDTO> books = getAllBooks();
        Deque<BookDTO> sortedBooksByAvailability = new LinkedList<>();
        for (BookDTO book : books) {
            if (book.getStatus().equals(BookStatus.AVAILABLE)) {
                sortedBooksByAvailability.addFirst(book);
            } else {
                sortedBooksByAvailability.addLast(book);
            }
        }
        return sortedBooksByAvailability.stream()
                .toList();
    }
}

