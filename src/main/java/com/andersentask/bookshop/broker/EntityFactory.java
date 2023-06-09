package com.andersentask.bookshop.broker;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class EntityFactory {

    public Request buildRequest(Book book) {
        return Request.builder()
                .book(book)
                .build();
    }

    public Order buildOrder(List<Book> books) {
        return Order.builder()
                .cost(getCostOfListOfBooks(books))
                .status(OrderStatus.IN_PROCESS)
                .books(books)
                .build();
    }

    private BigDecimal getCostOfListOfBooks(List<Book> books) {
        return books.stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


