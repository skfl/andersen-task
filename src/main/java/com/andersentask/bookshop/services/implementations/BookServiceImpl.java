package com.andersentask.bookshop.services.implementations;

import com.andersentask.bookshop.dtos.BookDTO;
import com.andersentask.bookshop.mappers.BookMapper;
import com.andersentask.bookshop.repositories.BookRepository;
import com.andersentask.bookshop.services.interfaces.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

