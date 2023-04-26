package com.andersentask.bookshop.controllers;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.broker.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final Commands commands;

    @GetMapping("all")
    public ResponseEntity<List<Book>> getBookList(@RequestParam(name = "sort", defaultValue = "ID") String sort) {
        return new ResponseEntity<>(
                commands.getSortedBooks(BookSort.valueOf(sort.toUpperCase(Locale.ROOT))),
                HttpStatus.OK);
    }

    @PostMapping("{bookId}/status")
    public ResponseEntity<Map<String, String>> changeBookStatus(@PathVariable Long bookId, @RequestBody Map<String, BookStatus> body) {
        Map<String, String> response = new HashMap<>();
        String result = commands.setStatusToBookAndDeleteCorrespondingRequests(bookId, body.get("status"))
                .toString();
        response.put("operationResult", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
