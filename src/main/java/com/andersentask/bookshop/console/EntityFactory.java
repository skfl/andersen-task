package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;

import java.math.BigDecimal;
import java.util.List;


public class EntityFactory {

    public Request buildRequest(Book book) {
        return Request.builder()
                .book(book)
                .build();
    }

    public Order buildOrder(List<Book> books) {
        return Order.builder()
                .orderCost(getCostOfListOfBooks(books))
                .orderStatus(OrderStatus.IN_PROCESS)
                .booksInOrder(books)
                .build();
    }

    private BigDecimal getCostOfListOfBooks(List<Book> books) {
        return books.stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


