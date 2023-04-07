package com.andersentask.bookshop.book.services.implementations;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.repositories.BookCollectionRepositoryImpl;
import com.andersentask.bookshop.book.services.interfaces.BookService;
import com.andersentask.bookshop.book.dtos.BookDTO;
import com.andersentask.bookshop.book.mappers.BookMapper;
import com.andersentask.bookshop.book.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookCollectionRepositoryImpl bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return BookMapper.entityListToDtoList(bookRepository.findAll());
    }

    @Override
    public List<BookDTO> getBooksSortedByName() {
        List<BookDTO> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(x -> x.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksSortedByPrice() {
        List<BookDTO> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparing(BookDTO::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksSortedByAvailability() {
        List<BookDTO> books = getAllBooks();
        return books.stream()
                .sorted(Comparator.comparingInt(x -> x.getStatus().getOrdinal()))
                .collect(Collectors.toList());
    }
}

