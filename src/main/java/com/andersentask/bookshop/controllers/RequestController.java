package com.andersentask.bookshop.controllers;

import com.andersentask.bookshop.broker.Commands;
import com.andersentask.bookshop.broker.enums.ResultOfOperation;
import com.andersentask.bookshop.request.entities.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final Commands commands;

    @GetMapping("all")
    public List<Request> getRequestList() {
        return commands.getAllRequests();
    }

    @GetMapping("map")
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        return commands.getBooksAndNumberOfRequests();
    }

    @PostMapping("new")
    public ResultOfOperation.CreateRequest createRequest(@RequestBody Map<String, Long> body) {
        return commands.createRequest(body.get("bookId"));
    }

    @GetMapping("book/{id}")
    public Optional<Long> getNumberOfRequestsOnBook(@PathVariable Long id) {
        return commands.getNumberOfRequestsOnBook(id);
    }
}
