package com.andersentask.bookshop.controllers;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.broker.Commands;
import com.andersentask.bookshop.broker.enums.ResultOfOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final Commands commands;

    @GetMapping("/all")
    public List<Book> getBookList(@RequestParam(name = "sort", defaultValue = "ID") String sort) {
        return commands.getSortedBooks(BookSort.valueOf(sort.toUpperCase(Locale.ROOT)));
    }

    @PostMapping("/{bookId}/status")
    public ResultOfOperation.SetBookStatus changeBookStatus(@PathVariable Long bookId, @RequestBody Map<String, BookStatus> body) {
        return commands.setStatusToBookAndDeleteCorrespondingRequests(bookId, body.get("status"));
    }
}
