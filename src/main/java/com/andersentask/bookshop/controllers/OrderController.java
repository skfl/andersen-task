package com.andersentask.bookshop.controllers;

import com.andersentask.bookshop.broker.Commands;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
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
@RequestMapping("/order")
public class OrderController {

    private final Commands commands;

    @GetMapping("all")
    public ResponseEntity<List<Order>> getOrderList(@RequestParam(name = "sort", defaultValue = "ID") String sort) {
        return new ResponseEntity<>(
                commands.getSortedOrders(OrderSort.valueOf(sort.toUpperCase(Locale.ROOT))),
                HttpStatus.OK);
    }

    @PostMapping("new")
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, List<Long>> body) {
        Map<String, String> response = new HashMap<>();
        String result = commands.createOrder(body.get("bookIds"))
                .toString();
        response.put("operationResult", result);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getBooksFromOrder(@PathVariable Long orderId){
        return new ResponseEntity<>(
                commands.getOrderById(orderId),
                HttpStatus.OK);
    }

    @PostMapping("{orderId}/status")
    public ResponseEntity<Map<String,String>> changeOrderStatus(
            @PathVariable Long orderId, @RequestBody Map<String, OrderStatus> body){
        Map<String, String> response = new HashMap<>();
        String result = commands.changeStatusOfOrderIncludingBooksCheck(orderId, body.get("status"))
                .toString();
        response.put("operationResult", result);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
