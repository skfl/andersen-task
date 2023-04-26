package com.andersentask.bookshop.controllers;

import com.andersentask.bookshop.broker.Commands;
import com.andersentask.bookshop.request.entities.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final Commands commands;

    @GetMapping("all")
    public ResponseEntity<List<Request>> getRequestList() {
        return new ResponseEntity<>(commands.getAllRequests()
                , HttpStatus.OK);
    }

    @GetMapping("map")
    public ResponseEntity<Map<Long, Long>> getBooksAndNumberOfRequests() {
        return new ResponseEntity<>(commands.getBooksAndNumberOfRequests(),
                HttpStatus.OK);
    }

    @PostMapping("new")
    public ResponseEntity<Map<String,String>> createRequest(@RequestBody Map<String, Long> body) {
        Map<String, String> response = new HashMap<>();
        String result = commands.createRequest(body.get("bookId"))
                .toString();
        response.put("operationResult", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("book/{id}")
    public ResponseEntity<Long> getNumberOfRequestsOnBook(@PathVariable Long id) {
        return new ResponseEntity<>(commands.getNumberOfRequestsOnBook(id),
                HttpStatus.OK);
    }
}
